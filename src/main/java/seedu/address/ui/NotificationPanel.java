package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.notification.Notification;

//@@author snookerballs
/**
 * Panel containing the notifications.
 */
public class NotificationPanel extends UiPart<Region> {
    private static final String FXML = "NotificationPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(NotificationPanel.class);

    @FXML
    private ListView<Notification> notificationListView;

    public NotificationPanel(ObservableList<Notification> notificationList) {
        super(FXML);
        setConnections(notificationList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Notification> notificationList) {
        ArrayList<Notification> list = new ArrayList<>();
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            list.add(notificationList.get(i));
        }
        ObservableList<Notification> reversedList = FXCollections.observableArrayList(list);
        notificationListView.setItems(reversedList);
        notificationListView.setCellFactory(listView -> new NotificationPanel.NotificationListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        notificationListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    logger.fine("Selection in expense list panel changed to : '" + newValue + "'");
                    //raise(new ExpensePanelSelectionChangedEvent(newValue));
                }
            });
    }

    /**
    * Scrolls to the {@code ExpenseCard} at the {@code index} and selects it.
    */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            notificationListView.scrollTo(index);
            notificationListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Expense} using a {@code ExpenseCard}.
     */
    class NotificationListViewCell extends ListCell<Notification> {
        @Override
        protected void updateItem(Notification notification, boolean empty) {
            super.updateItem(notification, empty);
            if (empty || notification == null) {
                setGraphic(null);
                setText(null);
            } else {
                Platform.runLater(() -> setGraphic(new NotificationCard(notification).getRoot()));
            }
        }
    }
}
