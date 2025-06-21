package ded.async.dedservice.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String normalizeJson(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            JsonNode normalizedNode = normalizeJsonNode(node);
            return mapper.writeValueAsString(normalizedNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to normalize JSON", e);
        }
    }

    private static JsonNode normalizeJsonNode(JsonNode node) throws JsonProcessingException {
        if (node.isObject()) {
            Map<String, JsonNode> sortedFields = new TreeMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                sortedFields.put(entry.getKey(), normalizeJsonNode(entry.getValue()));
            }
            
            ObjectNode sortedNode = mapper.createObjectNode();
            sortedFields.forEach(sortedNode::set);
            return sortedNode;
        }
        return node;
    }
}