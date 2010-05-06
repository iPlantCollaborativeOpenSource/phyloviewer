package org.iplantc.de.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Constants used by the client to communicate system errors. 
 *
 * All values present here are subject to translation for internationalization.
 */
public interface DEErrorStrings extends Constants
{
	String error();

	String loginFailed();

	String unableToBuildWorkspace();

	String renameFolderFailed();

	String renameFileFailed();

	String deleteFolderFailed();

	String deleteFileFailed();

	String createFolderFailed();

	String retrieveFiletreeFailed();

	String retrieveUserInfoFailed();

	String rawDataSaveFailed();

	String fileUploadFailed();

	String importFailed();

	String searchFailed();

	String mustSelectBeforeImport();

	String treeServiceRetrievalFailed();

	String saveJobError();

	String getJobsError();

	String deleteJobError();

	String runJobError();

	String moveFileFailed();

	String downloadResultError();
}
