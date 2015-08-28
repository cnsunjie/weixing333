package wx.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.NumericNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.*;

public class JsonWrapper {

	private JsonNode root;

	public JsonWrapper(String json) {
		newJsonWrapper(json, false);
	}

	/**
	 * 构造json
	 * 
	 * @param json
	 * @param allowUnquotedControlChars
	 *            允许出现特殊字符和转义符
	 * @throws Exception
	 */
	public JsonWrapper(String json, boolean allowUnquotedControlChars) {
		newJsonWrapper(json, allowUnquotedControlChars);
	}

	private void newJsonWrapper(String json, boolean allowUnquotedControlChars) {
		try {
			if (json != null && !json.trim().isEmpty()) {

				ObjectMapper mapper = new ObjectMapper();
				if (allowUnquotedControlChars) {
					// 允许出现特殊字符和转义符
					mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
				}
				org.codehaus.jackson.JsonNode node = mapper.readTree(json);
				root = node;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public JsonWrapper(JsonNode root) {
		this.root = root;
	}

	public String get(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		return (node == null ? null : node.getTextValue());
	}

	public long[] getLongArr(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		if (null == node) {
			return new long[0];
		}
		List<Long> result = new ArrayList<Long>();
		Iterator<org.codehaus.jackson.JsonNode> iter = node.getElements();
		while (iter.hasNext()) {
			org.codehaus.jackson.JsonNode next = iter.next();
			long nextValue = next.getLongValue();
			result.add(nextValue);
		}
		return toLongArr(result);
	}

	/**
	 * moved from ArrayUtils
	 */
	private static long[] toLongArr(Collection<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return new long[0];
		}
		long[] idsArr = new long[ids.size()];
		int idx = 0;
		for (long id : ids) {
			idsArr[idx++] = id;
		}
		return idsArr;
	}

	public String[] getStringArr(String name) {
		List<JsonNode> result = getList(name);
		String[] strs = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			JsonNode node = result.get(i);
			strs[i] = node.getTextValue();
		}
		return strs;
	}

	/**
	 * 返回json字符串中的json数组，并包装为JsonWrapper。key对应的数组不存在或类型不是数组则返回null<br>
	 * 如json：{"code":0,"results":[{"uid":10504,"caps":1},{"uid":11052,"caps":1}]
	 * }，要取"results" 数组中第一个json对象的uid，可以使用<br>
	 * <p>
	 * JsonWrapper[] jsonWrappers = new
	 * JsonWrapper(json).getJsonWrapperArray("results"); <br/>
	 * int uid = jsonWrappers[0].getInt("uid"); <br/>
	 * </p>
	 * 结果为10504
	 * 
	 * @param name
	 *            json数组的key
	 * @return JsonWrapper数组，数组中每一个元素对应一个json数组中的元素
	 */
	public JsonWrapper[] getJsonWrapperArr(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		if (node == null) {
			return null;
		}
		if (!node.isArray()) {
			return null;
		}
		JsonWrapper[] jsonWrappers = new JsonWrapper[node.size()];
		Iterator<org.codehaus.jackson.JsonNode> iter = node.getElements();
		int i = 0;
		while (iter.hasNext()) {
			org.codehaus.jackson.JsonNode next = iter.next();
			jsonWrappers[i] = new JsonWrapper(next);
			i++;
		}
		return jsonWrappers;
	}

	private List<JsonNode> getList(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);

		List<JsonNode> result = new ArrayList<JsonNode>();
		if (null == node) {
			return result;
		}
		Iterator<org.codehaus.jackson.JsonNode> iter = node.getElements();
		while (iter.hasNext()) {
			org.codehaus.jackson.JsonNode next = iter.next();
			if (next != null) {
				result.add(next);
			}
		}
		return result;
	}

	public String getString(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		return (node == null ? null : node.getTextValue());
	}

	public long getLong(String name) {
		return getLong(name, 0l);
	}

	public long getLong(String name, long defaultValue) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		if (node == null)
			return defaultValue;
		else if (node instanceof NumericNode)
			return ((NumericNode) node).getLongValue();
		else
			return str2long(node.getTextValue(), defaultValue);
	}

	public int getInt(String name) {
		return getInt(name, 0);
	}

	public int getInt(String name, int defaultValue) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		if (node == null)
			return defaultValue;
		else if (node instanceof NumericNode)
			return ((NumericNode) node).getIntValue();
		else
			return str2int(node.getTextValue(), defaultValue);
	}

	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		if (node == null)
			return defaultValue;
		else if (node instanceof BooleanNode)
			return ((BooleanNode) node).getBooleanValue();
		else
			return Boolean.parseBoolean(node.getTextValue());
	}

	public JsonWrapper getNode(String name) {
		return new JsonWrapper(getJsonNode(name));
	}

	public JsonNode getRootNode() {
		return root;
	}

	public boolean isEmpty() {
		return (root == null || root.size() == 0);
	}

	public JsonNode getJsonNode(String name) {
		org.codehaus.jackson.JsonNode node = getRawJsonNode(name);
		return node;
	}

	public org.codehaus.jackson.JsonNode getRawJsonNode(String name) {
		if (name == null || root == null)
			return null;

		org.codehaus.jackson.JsonNode node = root;
		StringTokenizer st = new StringTokenizer(name, ".");
		while (st.hasMoreTokens()) {
			String key = st.nextToken().trim();
			if (key.isEmpty() || (node = node.get(key)) == null)
				return null;
		}
		return node;
	}

	public String toString() {
		if (root == null) {
			return null;
		}
		return root.toString();
	}

	private int str2int(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private long str2long(String s, long defaultValue) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public boolean isValueNode() {
		return root.isValueNode();
	}

	public boolean isContainerNode() {
		return root.isContainerNode();
	}

	public boolean isMissingNode() {
		return root.isMissingNode();
	}

	public boolean isArray() {
		return root.isArray();
	}

	public boolean isObject() {
		return root.isObject();
	}

	public boolean isNumber() {
		return root.isNumber();
	}

	public boolean isIntegralNumber() {
		return root.isIntegralNumber();
	}

	public boolean isFloatingPointNumber() {
		return root.isFloatingPointNumber();
	}

	public boolean isInt() {
		return root.isInt();
	}

	public boolean isLong() {
		return root.isLong();
	}

	public boolean isDouble() {
		return root.isDouble();
	}

	public boolean isBigDecimal() {
		return root.isBigDecimal();
	}

	public boolean isTextual() {
		return root.isTextual();
	}

	public boolean isBoolean() {
		return root.isBoolean();
	}

	public boolean isNull() {
		return root.isNull();
	}
	
    private ObjectNode getRootAsObjectNode() {
        return (ObjectNode)getRootNode();
    }
    
    public void remove(String name) {
        if (getRootNode() != null) {
            getRootAsObjectNode().remove(name);
        }
    }
    
    public JsonWrapper getObject(String fieldName) {
        if (getRootNode() != null) {
            JsonNode node = getNode(fieldName).getRootNode();
            if (node != null) {
                return new JsonWrapper(node);
            }
        }
        return null;
    }
    
    public JsonWrapper putObject(String fieldName) {
        if (getRootNode() != null) {
            ObjectNode node = getRootAsObjectNode().putObject(fieldName);
            return new JsonWrapper(node);
        }  
        return null;
    }
    
    public void put(String name, String value) {
        if (getRootNode() != null) {
            getRootAsObjectNode().put(name, value);
        }
    }
    
    public int getArraySize() {
        if (getRootNode() != null && getRootNode() instanceof ArrayNode) {
            return ((ArrayNode)getRootNode()).size();
        }
        return 0;
    }
    
    public JsonWrapper getArrayNode(int index) {
        if (getRootNode() != null && getRootNode() instanceof ArrayNode) {
            return new JsonWrapper(((ArrayNode)getRootNode()).get(index));
        }
        return null;
    }
    
    public void addArrayElement(JsonWrapper element) {
        if (getRootNode() != null && getRootNode() instanceof ArrayNode) {
            ((ArrayNode)getRootNode()).add(element.getRootNode());
        }
    }
    
    public void insertArrayElement(int index, JsonWrapper element) {
        if (getRootNode() != null && getRootNode() instanceof ArrayNode) {
            ((ArrayNode)getRootNode()).insert(index, element.getRootNode());
        }
    }
    
    public void put(String name, long value) {
        if (getRootNode() != null) {
            getRootAsObjectNode().put(name, value);
        }
    }
    
    public String toJson() {
        return getRootNode().toString();
    }
}
