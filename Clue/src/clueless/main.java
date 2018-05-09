import javax.swing.*;



public class main extends JFrame {
    public main() {
        setTitle("Make Suggestion");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new box();

        this.add(panel);

    }
    public static void main(String[] args) {
        JFrame frame = new main();
        frame.setVisible(true);
    }
}





