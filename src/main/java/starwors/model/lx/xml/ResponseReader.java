/*
 * © EPAM Systems, 2012  
 */
package starwors.model.lx.xml;

import starwors.model.lx.bot.Response;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;

/**
 * Преобразовывает XML из входного потока в список плнет
 */
public class ResponseReader {

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

	/**
	 * Преобразовывает XML из входного потока в список планет (и ошибок) и заносит их в специальный объект
	 *
	 * @param input входной поток, из которого будет считываться XML
	 */
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

	private static String getTagText(XMLStreamReader reader, String tagName) throws XMLStreamException, ResponseReadException {
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

}
