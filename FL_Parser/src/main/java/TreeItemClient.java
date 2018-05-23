import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

public class TreeItemClient extends TreeItem<Label> {
    Tree.Vertex ver;

    public TreeItemClient(Tree.Vertex ver) {
        super(new Label(ver.content == null ? "" : ver.content.toString()));
        this.ver = ver;
    }

    @Override
    public ObservableList<TreeItem<Label>> getChildren() {
        ObservableList<TreeItem<Label>> children = super.getChildren();
        if (children.size() == 0) {
            for (Tree.Vertex v : ver.children) {
                children.add(new TreeItemClient(v));
            }
        }
        return children;
    }

    @Override
    public boolean isLeaf() {
        return ver.children.size() == 0;
    }
}