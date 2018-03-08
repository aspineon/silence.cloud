package helpers;

import models.core.StatusModel;

import java.util.Date;
import java.util.List;

public class DefaultStatuses {

    private Long activeStatusId = 1L;
    private Long inactiveStatusId = 2L;
    private Long frozenStatusId = 3L;
    private Long bannedStatusId = 4L;
    private Long blockedStatusId = 5L;
    private Long onlineStatusId = 6L;
    private Long offlineStatusId = 7L;

    private String activeStatusName = "active";
    private String inactiveStatusName = "inactive";
    private String frozenStatusName = "frozen";
    private String bannedStatusName = "banned";
    private String blockedStatusName = "blocked";
    private String onlineStatusName = "online";
    private String offlineStatusName = "offline";

    public void createDefaultStatuses() {
        createStatus(activeStatusId, activeStatusName);
        createStatus(inactiveStatusId, inactiveStatusName);
        createStatus(frozenStatusId, frozenStatusName);
        createStatus(bannedStatusId, bannedStatusName);
        createStatus(blockedStatusId, blockedStatusName);
        createStatus(onlineStatusId, onlineStatusName);
        createStatus(offlineStatusId, offlineStatusName);
    }

    public void deleteDefaultStatuses() {
        List<StatusModel> statusModels = StatusModel.FINDER.all();
        for (StatusModel statusModel : statusModels) {
            statusModel.delete();
        }
    }

    private void createStatus(Long id, String name) {
        StatusModel statusModel = new StatusModel();
        statusModel.id = id;
        statusModel.name = name;
        statusModel.createdAt = new Date();
        statusModel.updatedAt = new Date();
        statusModel.save();
    }

}
