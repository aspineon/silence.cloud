package models.core;

import java.util.List;

public interface StatusModelInterface {

    void createStatus(String name);

    void updateStatus(Long id, String name);

    void deleteStatus(Long id);

    List<StatusModel> findAllStatuses();

    StatusModel findStatusById(Long id);

    StatusModel findStatusByName(String name);

}
