package com.br.model.services.inner.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.game.api.galaxy.Planet;
import com.br.game.api.galaxy.PlanetType;
import com.br.model.services.inner.IReaderService;
import com.br.model.services.inner.beans.Response;
import com.br.model.services.inner.exception.ResponseReadException;
import com.br.starwors.lx.galaxy.Action;

import java.io.InputStream;
import java.util.*;

/**
 * Преобразовывает XML из входного потока в список планет
 */
@Component
@Instantiate
@Provides
public class XmlReaderService implements IReaderService {

	// XML response tags
	// root
	private static final String RESPONSE = "response";

	// subroot
	private static final String PLANETS = "planets";
	private static final String ERRORS = "errors";

	// planet description
	private static final String PLANET = "planet";
	private static final String ID = "id";
	private static final String OWNER = "owner";
	private static final String TYPE = "type";
	private static final String UNITS = "droids";
	private static final String NEIGHBOURS = "neighbours";
	private static final String NEIGHBOUR = "neighbour";

	// error message
	private static final String ERROR = "error";
	
	
	@Requires(optional = true)
	private LogService logService;
	
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	

	/**
	 * Преобразовывает XML из входного потока в список планет (и ошибок) и заносит их в специальный объект
	 *
	 * @param input входной поток, из которого будет считываться XML
	 */
	@Override
	public Response readGalaxy(InputStream input) throws ResponseReadException {
		Response result = null;
		XMLStreamReader reader = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			reader = factory.createXMLStreamReader(input);

			reader.nextTag();

			if (RESPONSE.equals(reader.getName().getLocalPart())) {
				result = parseResponse(reader);
			} else {
				throw new ResponseReadException(RESPONSE + " element expected");
			}

			return result;
		} catch (XMLStreamException ex) {
			throw new ResponseReadException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException ex) {
					// do nothing
				}
			}
		}
	}

	@Override
	public Collection<Action> readActions(InputStream input) throws ResponseReadException {
		return ActionReader.readActions(input);
	}

	private Response parseResponse(XMLStreamReader reader) throws XMLStreamException, ResponseReadException {
		Response result = new Response();
		reader.nextTag();

		if (PLANETS.equals(reader.getName().getLocalPart())) {
			Collection<Planet> planets = parsePlanets(reader);
			for (Planet planet : planets) {
				result.addPlanet(planet);
			}
		} else {
			throw new ResponseReadException(PLANETS + " element expected");
		}

		if (ERRORS.equals(reader.getName().getLocalPart())) {
			Collection<String> errors = parseErrors(reader);
			for (String error: errors) {
				result.addError(error);
			}
		} else {
			throw new ResponseReadException(ERRORS + " element expected");
		}
		return result;
	}

	private Collection<Planet> parsePlanets(XMLStreamReader reader) throws XMLStreamException, ResponseReadException {
		Collection<Planet> planets = new ArrayList<Planet>();
		if (reader.nextTag() != XMLStreamReader.END_ELEMENT) {
			Map<String, Planet> planetsByID = new HashMap<String, Planet>();
			do {
				if (PLANET.equals(reader.getName().getLocalPart())) {
					planets.add(parsePlanet(reader, planetsByID));
				} else {
					throw new ResponseReadException(PLANET + " element expected");
				}
			} while (reader.isStartElement());
		}
		reader.nextTag();
		return planets;
	}

	private Planet parsePlanet(XMLStreamReader reader, Map<String, Planet> planetsByID) throws XMLStreamException, ResponseReadException {
		String planetId = reader.getAttributeValue(null, ID);
		Planet planet = getPlanetByID(planetsByID, planetId);

		reader.nextTag();
		planet.setOwner(getTagText(reader, OWNER));
		PlanetType type = parsePlanetType(getTagText(reader, TYPE));
		planet.setType(type);
		planet.setUnits(Integer.parseInt(getTagText(reader, UNITS)));

		if (NEIGHBOURS.equals(reader.getName().getLocalPart())) {
			parseNeighbours(reader, planetsByID, planet);
		} else {
			throw new ResponseReadException(NEIGHBOURS + " element expected");
		}
		reader.nextTag();
		return planet;
	}

	private void parseNeighbours(XMLStreamReader reader, Map<String, Planet> planetsByID, Planet planet) throws XMLStreamException, ResponseReadException {
		if (reader.nextTag() == XMLStreamReader.END_ELEMENT) {
			return;
		}

		do {
			String neighbourId = getTagText(reader, NEIGHBOUR);
			Planet neighbour = getPlanetByID(planetsByID, neighbourId);
			planet.addNeighbours(neighbour);
		} while (reader.isStartElement());
		reader.nextTag();
	}
	
	private Planet getPlanetByID(Map<String, Planet> planetsByID, String id) {
		Planet planet = planetsByID.get(id);
		if (planet == null) {
			planet = new Planet(id);
			planetsByID.put(id, planet);
		}
		return planet;
	}

	private Collection<String> parseErrors(XMLStreamReader reader) throws XMLStreamException, ResponseReadException {
		Collection<String> errors = new ArrayList<String>();
		if (reader.nextTag() != XMLStreamReader.END_ELEMENT) {
			
			do {
				if (ERROR.equals(reader.getName().getLocalPart())) {
					errors.add(getTagText(reader, ERROR));
				}
			} while (reader.isStartElement());
		}
		reader.nextTag();
		return errors;
	}

	public static String getTagText(XMLStreamReader reader, String tagName) throws XMLStreamException, ResponseReadException {
		String currentTagName = reader.getName().getLocalPart();

		if (reader.isStartElement() && tagName.equals(currentTagName)) {
			String textContent = reader.getElementText();
			reader.nextTag();
			return textContent;
		} else {
			throw new ResponseReadException(reader.getName().toString() + " found but " + tagName + " expected");
		}
	}

	private static PlanetType parsePlanetType(String s) {
		for (PlanetType type : PlanetType.values()) {
			if (type.name().equals(s)) {
				return type;
			}
		}
		throw new NoSuchElementException();
	}

	private static class ActionReader {

		// XML response tags
		// root
		private static final String ACTIONS = "actions";
		private static final String ACTION = "action";

		// subroot
		private static final String FROM = "from";
		private static final String TO = "to";
		private static final String UNITS_COUNT = "unitsCount";

		public static Collection<Action> readActions(InputStream input) throws ResponseReadException {
			List<Action> result = new LinkedList<Action>();
			XMLStreamReader reader = null;
			try {
				XMLInputFactory factory = XMLInputFactory.newFactory();
				reader = factory.createXMLStreamReader(input);
				reader.nextTag();

				if (ACTIONS.equals(reader.getName().getLocalPart())) {
					readAction(result, reader);
				} else {
					throw new ResponseReadException(ACTIONS + " element expected");
				}

				return Collections.unmodifiableList(result);
			} catch (XMLStreamException ex) {
				throw new ResponseReadException(ex);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (XMLStreamException ex) {
						// do nothing
					}
				}
			}

		}

		private static void readAction(List<Action> result, XMLStreamReader reader) throws XMLStreamException, ResponseReadException {

			if (reader.nextTag() != XMLStreamReader.END_ELEMENT) {
				do {
					if (ACTION.equals(reader.getName().getLocalPart())) {
						result.add(parseAction(reader));
					} else {
						throw new ResponseReadException(ACTION + " element expected");
					}
				} while (reader.isStartElement());
			}

		}

		private static Action parseAction(XMLStreamReader reader) throws XMLStreamException, ResponseReadException {
			Action move = new Action();
			reader.nextTag();
			move.setFrom(XmlReaderService.getTagText(reader, FROM));
			move.setTo(XmlReaderService.getTagText(reader, TO));
			move.setAmount(Integer.parseInt(XmlReaderService.getTagText(reader, UNITS_COUNT)));
			reader.nextTag();
			return move;
		}


	}

}
