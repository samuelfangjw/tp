package seedu.address.ui;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_NO_GROUPMATES_TO_DISPLAY;
import static seedu.address.commons.core.Messages.MESSAGE_NO_TODOS_TO_DISPLAY;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.groupmate.Groupmate;
import seedu.address.model.project.Project;
import seedu.address.model.task.CompletableDeadline;
import seedu.address.model.task.CompletableTodo;
import seedu.address.model.task.repeatable.Event;
import seedu.address.model.task.repeatable.RepeatableComparator;

/**
 * Panel containing a project.
 */
public class ProjectDisplayPanel extends UiPart<Region> {
    public static final Integer OVERVIEW_TAB = 0;
    public static final Integer TODOS_TAB = 1;

    private static final String FXML = "ProjectDisplayPanel.fxml";

    private static final int SAFETY_MARGIN = 5; // Applied to each listview to prevent card from being cut off
    private static final int EVENTS_CARD_HEIGHT = 45;
    private static final int DEADLINES_CARD_HEIGHT = 45;
    private static final int GROUPMATES_CARD_HEIGHT = 55;

    private final ListView<CompletableTodo> completableTodoListView = new ListView<>();
    private final ListView<Groupmate> groupmateListView = new ListView<>();

    @FXML
    private Label projectName;
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<Event> eventListView;
    @FXML
    private ListView<CompletableDeadline> completableDeadlineListView;
    @FXML
    private StackPane todoListViewPlaceholder;
    @FXML
    private StackPane groupmateListViewPlaceholder;

    /**
     * Creates a {@code ProjectDisplayPanel}.
     */
    public ProjectDisplayPanel() {
        super(FXML);
    }

    /**
     * Displays a project in the {@code ProjectDisplayPanel}.
     *
     * @param project Project to display.
     */
    public void displayProject(Project project) {
        requireNonNull(project);

        this.projectName.setText(project.getProjectName().toString());

        setUpEventList(project.getEvents().getEvents());
        setUpDeadlinesList(project.getDeadlines().getDeadlines());
        setUpTodoList(project.getTodos().getTodos());
        setUpGroupmatesList(project.getGroupmates().getGroupmates());
    }

    private void setUpTodoList(ObservableList<CompletableTodo> todos) {
        completableTodoListView.setItems(new FilteredList<>(todos));
        completableTodoListView.setCellFactory(listView -> new CompletableTodoListViewCell());

        todoListViewPlaceholder.getChildren().clear();
        if (completableTodoListView.getItems().isEmpty()) {
            Label noTodosPlaceholder = new Label();
            noTodosPlaceholder.setText(MESSAGE_NO_TODOS_TO_DISPLAY);
            todoListViewPlaceholder.getChildren().add(noTodosPlaceholder);
        } else {
            todoListViewPlaceholder.getChildren().add(completableTodoListView);
        }
    }

    private void setUpGroupmatesList(ObservableList<Groupmate> groupmates) {
        groupmateListView.prefHeightProperty()
                .bind(Bindings.size(groupmates).multiply(GROUPMATES_CARD_HEIGHT).add(SAFETY_MARGIN));
        groupmateListView.setItems(new FilteredList<>(groupmates));
        groupmateListView.setCellFactory(listView -> new GroupmateListViewCell());

        groupmateListViewPlaceholder.getChildren().clear();
        if (groupmateListView.getItems().isEmpty()) {
            Label noGroupmatesPlaceholder = new Label();
            noGroupmatesPlaceholder.setText(MESSAGE_NO_GROUPMATES_TO_DISPLAY);
            groupmateListViewPlaceholder.getChildren().add(noGroupmatesPlaceholder);
        } else {
            groupmateListViewPlaceholder.getChildren().add(groupmateListView);
        }
    }

    private void setUpDeadlinesList(ObservableList<CompletableDeadline> deadlines) {
        completableDeadlineListView.prefHeightProperty()
                .bind(Bindings.size(deadlines).multiply(DEADLINES_CARD_HEIGHT).add(SAFETY_MARGIN));
        completableDeadlineListView.setItems(new SortedList<>(deadlines,
                Comparator.comparing(CompletableDeadline::getBy).thenComparing(CompletableDeadline::getDescription)));
        completableDeadlineListView.setCellFactory(listView ->
                new CompletableDeadlineListViewCell());
    }

    private void setUpEventList(ObservableList<Event> events) {
        eventListView.prefHeightProperty()
                .bind(Bindings.size(events).multiply(EVENTS_CARD_HEIGHT).add(SAFETY_MARGIN));
        eventListView.setItems(new SortedList<>(events, new RepeatableComparator()));
        eventListView.setCellFactory(listView -> new EventListViewCell());
    }

    /**
     * Displays the overview tab.
     */
    public void showOverviewTab() {
        tabPane.getSelectionModel().select(OVERVIEW_TAB);
    }

    /**
     * Displays the todo tab.
     */
    public void showTodosTab() {
        tabPane.getSelectionModel().select(TODOS_TAB);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code Event} using an {@code EventCard}.
     */
    static class EventListViewCell extends ListCell<Event> {
        @Override
        protected void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new EventCard(event, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code CompletableDeadline} using
     * a {@code CompletableDeadlineCard}.
     */
    static class CompletableDeadlineListViewCell extends ListCell<CompletableDeadline> {
        @Override
        protected void updateItem(CompletableDeadline completableDeadline, boolean empty) {
            super.updateItem(completableDeadline, empty);

            if (empty || completableDeadline == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CompletableDeadlineCard(completableDeadline, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code CompletableTodo} using
     * a {@code CompletableTodoCard}.
     */
    static class CompletableTodoListViewCell extends ListCell<CompletableTodo> {
        @Override
        protected void updateItem(CompletableTodo completableTodo, boolean empty) {
            super.updateItem(completableTodo, empty);

            if (empty || completableTodo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CompletableTodoCard(completableTodo, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Groupmate} using a {@code GroupmateCard}.
     */
    static class GroupmateListViewCell extends ListCell<Groupmate> {
        @Override
        protected void updateItem(Groupmate groupmate, boolean empty) {
            super.updateItem(groupmate, empty);

            if (empty || groupmate == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new GroupmateCard(groupmate, getIndex() + 1).getRoot());
            }
        }
    }
}
