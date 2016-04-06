package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2bean.FastaDigestionBeanAdapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2bean.ServerTypeBeanAdapter;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;

public class DataSourceBeanAdapter implements Adapter<DataSourceBean> {

	private final FileType file;
	private final ServersType servers;

	public DataSourceBeanAdapter(FileType fileType, ServersType serversType) {
		file = fileType;
		servers = serversType;
	}

	@Override
	public DataSourceBean adapt() {
		DataSourceBean ret = new DataSourceBean();
		ret.setFileName(file.getName());
		if (file.getFormat() != null) {
			final FileFormat fileFormatFromString = FileFormat.getFileFormatFromString(file.getFormat().name());
			ret.setFormat(fileFormatFromString);
		}
		ret.setId(file.getId());
		ret.setRelativePath(file.getRelativePath());
		final ServerType referencedServer = ImportCfgUtil.getReferencedServer(file.getServerRef(), servers);
		if (referencedServer != null) {
			ret.setServer(new ServerTypeBeanAdapter(referencedServer).adapt());
		}
		if (file.getFastaDigestion() != null)
			ret.setFastaDigestionBean(new FastaDigestionBeanAdapter(file.getFastaDigestion()).adapt());
		ret.setUrl(file.getUrl());
		return ret;
	}

}
