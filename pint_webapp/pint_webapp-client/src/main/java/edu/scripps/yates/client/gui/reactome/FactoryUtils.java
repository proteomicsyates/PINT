package edu.scripps.yates.client.gui.reactome;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FactoryUtils {

	public static Boolean getBooleanValue(JSONObject jsonObject, String key) {
		JSONValue aux = jsonObject.get(key);
		if (aux.isBoolean() != null) {
			return aux.isBoolean().booleanValue();
		}
		if (aux.isString() != null) {
			return Boolean.valueOf(aux.isString().stringValue());
		}
		return null;
	}

	public static Integer getIntValue(JSONObject jsonObject, String key) {
		JSONValue aux = jsonObject.get(key);
		if (aux.isNumber() != null) {
			return (int) aux.isNumber().doubleValue();
		}
		if (aux.isString() != null) {
			return Integer.valueOf(aux.isString().stringValue());
		}
		return null;
	}

	public static Long getLongValue(JSONObject jsonObject, String key) {
		JSONValue aux = jsonObject.get(key);
		if (aux.isNumber() != null) {
			return (long) aux.isNumber().doubleValue();
		}
		if (aux.isString() != null) {
			return Long.valueOf(aux.isString().stringValue());
		}
		return null;
	}

	public static List<JSONObject> getObjectList(JSONObject jsonObject, String key) {
		List<JSONObject> list = new LinkedList<JSONObject>();
		JSONValue aux = jsonObject.get(key);
		if (aux != null) {
			JSONArray listAux = aux.isArray();
			if (listAux != null) {
				for (int i = 0; i < listAux.size(); ++i) {
					list.add(listAux.get(i).isObject());
				}
			} else {
				list.add(aux.isObject());
			}
		}
		return list;
	}

	public static List<String> getStringList(JSONObject jsonObject, String key) {
		List<String> list = new LinkedList<String>();
		JSONValue aux = jsonObject.get(key);
		if (aux != null) {
			if (aux.isString() != null) {
				list.add(aux.isString().stringValue());
			} else {
				JSONArray listAux = aux.isArray();
				if (listAux != null) {
					for (int i = 0; i < listAux.size(); ++i) {
						list.add(listAux.get(i).isString().stringValue());
					}
				}
			}
		}
		return list;
	}

	public static String getStringValue(JSONObject jsonObject, String key) {
		JSONString aux = jsonObject.get(key).isString();
		if (aux != null) {
			return aux.stringValue();
		}
		return null;
	}
}
