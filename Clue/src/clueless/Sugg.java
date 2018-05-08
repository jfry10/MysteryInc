import javax.swing.*;



public class Sugg extends JFrame {
    public Sugg() {
        setTitle("Make Suggestion");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new suggestionBox();

        this.add(panel);
    }
    public static void main(String[] args) {
        JFrame frame = new Sugg();
        frame.setVisible(true);
    }
}


