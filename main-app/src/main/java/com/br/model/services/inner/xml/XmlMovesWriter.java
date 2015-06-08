package com.br.model.services.inner.xml;


import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.game.api.galaxy.Move;
import com.br.model.services.inner.IMovesWriterService;
import com.br.model.services.inner.exception.MovesWriteException;

import java.io.OutputStream;
import java.util.Collection;

/**
 * Преобразует список команд в XML ответ для сервера
 */

@Component
@Instantiate
@Provides
public final class XmlMovesWriter implements IMovesWriterService {
	
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
	

	/* (non-Javadoc)
	 * @see com.br.model.services.xml.MovesWriterService#writeMoves(java.io.OutputStream, java.util.Collection)
	 */
	@Override
	public void writeMoves(OutputStream output, Collection<Move> moves, String token) throws MovesWriteException {
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
					logService.log(LogService.LOG_ERROR, "moves writer ex", e);
				}
			}
		}
	}

}
