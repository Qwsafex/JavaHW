package client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.SimpleFile;

import java.util.ArrayList;
import java.util.List;

public class SceneFactory {
    private static class VBoxBuilder {
        private List<Node> nodeList;

        public VBoxBuilder() {
            this.nodeList = new ArrayList<>();
        }

        private VBoxBuilder addNodesHorizontally(Node... nodes) {
            nodeList.add(new HBox(10, nodes));
            return this;
        }

        private VBox build() {
            Node[] nodes = new Node[nodeList.size()];
            nodes = nodeList.toArray(nodes);
            VBox vbox = new VBox(nodes);
            vbox.setMinWidth(MIN_WIDTH);
            vbox.setSpacing(10);
            return vbox;
        }
    }

    private static final double MIN_WIDTH = 400;

    public static Scene create() {
        VBoxBuilder vBoxBuilder = new VBoxBuilder();


        TableView<SimpleFile> table = new TableView<>();


        ObservableList<SimpleFile> teamMembers = FXCollections.observableArrayList();
        table.setItems(teamMembers);



        TableColumn<SimpleFile,String> firstNameCol = new TableColumn<>("Filename");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<SimpleFile,String> lastNameCol = new TableColumn<>("Is directory");
        lastNameCol.setCellValueFactory(new PropertyValueFactory("isDirectory"));

        table.getColumns().setAll(firstNameCol, lastNameCol);

        VBox root = vBoxBuilder.addNodesHorizontally(new Label("Hostname:"), new TextField(), new Label("Port:"), new TextField(), new Button("Connect"))
                .addNodesHorizontally(new Label("Current directory:"), new TextField())
                .addNodesHorizontally(table)
                .build();
        return new Scene(root);
    }

}
