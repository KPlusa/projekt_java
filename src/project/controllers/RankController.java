package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Rankclass;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "historia" dostepnej z poziomu menu*/
public class RankController extends Storage implements Initializable {
    private int counter;
    private Socket s;
    private String name;
    private String subject;
    private String sub;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    int id, points;
    final ObservableList mylist = FXCollections.observableArrayList();
    @FXML
    private ComboBox chb;
    @FXML
    TableView<Rankclass> table;
    @FXML
    private TableColumn<Rankclass, Integer> ColId;
    @FXML
    private TableColumn<Rankclass, String> ColSub;
    @FXML
    private TableColumn<Rankclass, String> ColName;
    @FXML
    private TableColumn<Rankclass, Integer> ColPoints;
    @FXML
    private TextField text;
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColSub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        try {
            table.setItems(fill_table());
        } catch (Exception e) {
        }
        try {
            fillcombo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Metoda wypelniajaca TableView odpowiednimi wartosciami
     *
     * @return Rankclass
     * @throws IOException wyjatek
     */
    @FXML
    private ObservableList<Rankclass> fill_table() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dos.writeInt(10);
                dos.writeUTF("");
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        dis.close();
        dos.close();
        s.close();
        return rank;
    }

    /**Metoda wypelniajaca ComboBox odpowiednimi wartosciami
     *
     * @throws IOException wyjatek
     */
    @FXML
    private void fillcombo() throws IOException {
        chb.setMaxHeight(30);
        mylist.clear();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Brak polaczenia z serwerem");
                }
                dos.writeInt(3);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    sub = dis.readUTF();
                    mylist.add(sub);
                }
                chb.setItems(mylist);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        dis.close();
        dos.close();
        s.close();

    }

    /**Metoda wywolujaca ustawiajaca wartosci w TableView
     *
     * @throws IOException wyjatek
     */
    @FXML
    private void setdisplay() throws IOException{
        table.setItems(Combo_fill_table());
    }

    /**Metoda wypelniajaca TableView odpowiednimi przy uwzglednieniu wyboru w ComboBox
     *
     * @return Rankclass
     * @throws IOException wyjatek
     */
    @FXML
    private ObservableList<Rankclass> Combo_fill_table() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        table.getItems().clear();
        sub = chb.getValue().toString();
        text.clear();
        try {
            while (true) {
                counter = 0;
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(11);
                dos.writeUTF(sub);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        dis.close();
        dos.close();
        s.close();
        return rank;
    }

    /**Metoda wywolujaca ustawiajaca wartosci w TableView przy uwzglednieniu wyboru w ComboBox
     *
     * @throws IOException wyjatek
     */
    @FXML
    private void setdisplaybyname() throws IOException{
        table.setItems(Combo_fill_table_by_name());
    }

    /**Metoda wypelniajaca TableView odpowiednimi przy uwzglednieniu wyboru w TextField
     *
     * @return Rankclass
     * @throws IOException wyjatek
     */
    @FXML
    private ObservableList<Rankclass> Combo_fill_table_by_name() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        table.getItems().clear();
        sub = text.getText();
        chb.setValue("");
        try {
            while (true) {
                counter = 0;
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(12);
                dos.writeUTF(sub);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        dis.close();
        dos.close();
        s.close();
        return rank;
    }
}