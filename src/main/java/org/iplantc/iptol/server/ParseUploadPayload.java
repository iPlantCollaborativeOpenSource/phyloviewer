package org.iplantc.iptol.server;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageAwareTransformer;

/**
 * When a file is uploaded, the Mule message contains the Content-Type and the
 * payload is the multipart containing the file contents.  Use the HTTP MultiPartParser
 * to extract the file contents.
 * @author Donald A. Barre
 */
public class ParseUploadPayload extends AbstractMessageAwareTransformer {

	private static final String WORKSPACE_ID = "workspaceId";
	private static final String FOLDER_ID = "folderId";
	private static final String DE_ADD_FILE_METHOD = "DE_ADD_FILE_METHOD";
	private static final String UPLOAD_METHOD = "upload";
	private static final String SAVE_AS_METHOD = "saveAs";

	/* (non-Javadoc)
	 * @see org.mule.transformer.AbstractMessageAwareTransformer#transform(org.mule.api.MuleMessage, java.lang.String)
	 */
	@Override
	public Object transform(MuleMessage message, String encoding)
			throws TransformerException {

		if (!(message.getPayload() instanceof String)) {
			throw new TransformerException(MessageFactory.createStaticMessage("Payload is not a string"));
		}

		String contentType = getContentType(message);
		if (contentType == null) {
			throw new TransformerException(MessageFactory.createStaticMessage("No content-type in multi-part request"));
		}

		MultiPartParser parser = new MultiPartParser(contentType, (String) message.getPayload());
		Parts parts = parser.getAllParts();
		FilePart filePart = parts.getFilePart("file");
		ParamPart copiedFromPart = parts.getParamPart("copied-from");

		/*
		 * The folder is optional.  If not specified, the default upload folder is used.
		 */
		Long folderId = null;
		if (message.getProperty(FOLDER_ID) != null) {
			folderId = getLongValue(message, FOLDER_ID);
		}

		/*
		 * Construct the new payload (file contents, filename, workspaceId, optional folderId).
		 */
		Object newPayload[] = null;

		if (copiedFromPart == null) {
			newPayload = new Object[folderId == null ? 3 : 4];
			newPayload[0] = filePart.getContents();
			newPayload[1] = filePart.getFilename();
			newPayload[2] = getLongValue(message, WORKSPACE_ID);
			if (folderId != null) {
				newPayload[3] = folderId;
			}
			message.setStringProperty(DE_ADD_FILE_METHOD, UPLOAD_METHOD);
		}
		else {
			newPayload = new Object[folderId == null ? 4 : 5];
			newPayload[0] = filePart.getContents();
			newPayload[1] = filePart.getFilename();
			newPayload[2] = Long.parseLong(copiedFromPart.getContents());
			newPayload[3] = getLongValue(message, WORKSPACE_ID);
			if (folderId != null) {
				newPayload[4] = folderId;
			}
			message.setStringProperty(DE_ADD_FILE_METHOD, SAVE_AS_METHOD);
		}
		return newPayload;
	}

	/**
	 * Get a Long property value.
	 * @param message
	 * @param name the name of the property
	 * @return the value or null if property name is not found
	 */
	private Long getLongValue(MuleMessage message, String name) {
		String value = message.getStringProperty(name, null);
		if (value == null) {
			return null;
		}
		return Long.valueOf(value);
	}

	/**
	 * Get the Content-Type from the message.  Handle the case where
	 * the property name can be any case.
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getContentType(MuleMessage message) {
		Set<String> names = message.getPropertyNames();
		for (String name : names) {
			if (StringUtils.equalsIgnoreCase(name, "content-type")) {
				return (String) message.getProperty(name);
			}
		}
		return null;
	}
}
