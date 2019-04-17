package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.ArrayList;
import java.util.List;

public class IDTableMapperRegistry {
	private static final List<IDTableMapper> idTableMappers = new ArrayList<IDTableMapper>();

	public static void register(IDTableMapper idTableMapper) {
		idTableMappers.add(idTableMapper);

	}

	public static void clearTableMappers() {
		for (final IDTableMapper idTableMapper : idTableMappers) {
			idTableMapper.clear();
		}
	}

}
