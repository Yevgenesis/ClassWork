package codezilla.hendynest.model;

import java.util.Map;

public record RequestDto (
        Map<String,String> map //String name, String value
) {}
