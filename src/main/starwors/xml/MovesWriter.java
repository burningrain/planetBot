/*
 * © EPAM Systems, 2012  
 */
package main.starwors.xml;

import java.io.OutputStream;
import java.util.Collection;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import main.starwors.galaxy.Move;

/**
 * Преобразует список команд в XML ответ для сервера
 */
public final class MovesWriter {

	private String token;

	public MovesWriter(String token) {
		this.token = token;
	}

	/**
	 * Преобразовывает список команд на передвижение в XML формат и записывает результат в указанный поток
	 * 
	 * @param output поток, в который будет записан результат
	 * @param moves список команд
	 */
	public void writeMoves(OutputStream output, Collection<Move> moves) throws MovesWriteException {
		XMLStreamWriter writer = null;
		try {
			XMLOutputFactory factory = XMLOutputFactory.newFactory();
			writer = factory.createXMLStreamWriter(output);

			writer.writeStartDocument();
			writer.writeStartElement("request");
			writer.writeStartElement("token");
			writer.writeCharacters(token);
			writer.writeEndElement();

			writer.writeStartElement("actions");
			for (Move move : moves) {
				writer.writeStartElement("action");
				writer.writeStartElement("from");
				writer.writeCharacters(move.getFrom().getId());
				writer.writeEndElement();
				writer.writeStartElement("to");
				writer.writeCharacters(move.getTo().getId());
				writer.writeEndElement();
				writer.writeStartElement("unitscount");
				writer.writeCharacters(Integer.toString(move.getAmount()));
				writer.writeEndElement();
				writer.writeEndElement();
			}
			writer.writeEndElement();
			writer.writeEndElement();
			writer.writeEndDocument();
		} catch (XMLStreamException ex) {
			throw new MovesWriteException(ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (XMLStreamException e) {
					// do nothing
				}
			}
		}
	}

}
