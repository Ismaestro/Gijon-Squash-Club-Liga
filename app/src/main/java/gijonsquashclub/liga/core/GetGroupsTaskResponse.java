package gijonsquashclub.liga.core;

import java.util.LinkedList;
import java.util.Map;

public interface GetGroupsTaskResponse {
    void injectGroups(Map<Integer, Map<String, LinkedList<Integer>>> groups);
}
