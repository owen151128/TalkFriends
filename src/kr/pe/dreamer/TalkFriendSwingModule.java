package kr.pe.dreamer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class TalkFriendSwingModule extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final String SPLASH_IMAGE = "splash.jpg";
	private JPanel panelMain = null;
	private JScrollPane scroll = null;
	private String target = null;
	private int scrollCount = 0;
	private String[] id = null;
	private String[] name = null;
	private String[] age = null;
	private String[] time = null;
	private String[] article = null;
	private String[] image = null;
	private String[] timeId = null;
	private String[] userId = null;
	private ArrayList<String> idList = null;
	private TalkFriendParseModule module = null;
	private String[][] result = null;
	private JLabel[] textLabel = null;
	private JButton[] addButton = null;
	private JPanel[] panel = null;
	private JPanel[] idPanel = null;
	private Image imageBuffer = null;
	private JLabel[] imageLabel = null;
	private JLabel[] idLabel = null;
	private JDialog splash = null;
	private JLabel background = null;
	private JProgressBar progress = null;
	private JLabel loadText = null;

	public TalkFriendSwingModule(String title) {
		super(title);
		splashInit();
		panelMain = new JPanel();
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private boolean splashInit() {
		loadText = new JLabel("불러오는중 ...");
		background = new JLabel(
				new ImageIcon(new ImageIcon(SPLASH_IMAGE).getImage().getScaledInstance(640, 480, Image.SCALE_DEFAULT)));
		splash = new JDialog((JFrame) null);
		progress = new JProgressBar();
		progress.setIndeterminate(true);
		background.setSize(600, 700);
		background.setLayout(new BorderLayout());
		loadText.setForeground(Color.BLACK);
		loadText.setFont(new Font("나눔고딕", Font.PLAIN, 30));
		loadText.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
		background.add(loadText);
		background.add(progress, BorderLayout.SOUTH);
		splash.add(background);
		splash.setAlwaysOnTop(true);
		splash.setModal(false);
		splash.setUndecorated(true);
		splash.pack();
		splash.setLocationRelativeTo(null);
		splash.setVisible(true);
		return true;
	}

	private boolean splashDestroy() {
		scrollCount *= 2;
		panelMain.setLayout(new GridLayout(scrollCount, 1));
		this.invalidate();
		this.validate();
		this.repaint();
		splash.setVisible(false);
		this.setVisible(true);
		return true;
	}

	public boolean initializeScreen(String search, boolean loopSw) {
		module = new TalkFriendParseModule("0", "9414065", "7cb27443127e05841cbdb0da2963c1d8");
		result = module.getList();
		name = result[0];
		age = result[1];
		time = result[2];
		article = result[3];
		image = result[4];
		timeId = result[5];
		userId = result[6];
		idList = new ArrayList<>();
		idList.addAll(Arrays.asList(userId));
		textLabel = new JLabel[name.length];
		panel = new JPanel[name.length];
		idLabel = new JLabel[name.length];
		idPanel = new JPanel[name.length];
		imageLabel = new JLabel[image.length];
		addButton = new JButton[name.length];

		loadText.setText("불러오는중 ... " + time[time.length - 1] + " / " + name[name.length - 1]);

		imageLabel = new JLabel[image.length];
		imageBuffer = null;

		for (int i = 0; i < name.length; i++) {
			addButton[i] = new JButton("친구 추가 요청" + " (" + userId[i] + ")");
			addButton[i].addActionListener(this);
			textLabel[i] = new JLabel(
					"<html>" + name[i] + " / " + age[i] + " / " + time[i] + " / " + "<br/>" + article[i] + "</html>");
			textLabel[i].setFont(new Font("나눔고딕", Font.PLAIN, 14));
			try {
				URL url = new URL(image[i]);
				imageBuffer = ImageIO.read(url);
				imageLabel[i] = new JLabel(new ImageIcon(imageBuffer));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < panel.length; i++) {
			panel[i] = new JPanel();
			panel[i].add(imageLabel[i]);
			panel[i].add(textLabel[i]);
			panel[i].add(addButton[i]);
		}

		for (int i = 0; i < name.length; i++) {
			id = module.getUserInfo(userId[i]);
			idLabel[i] = new JLabel();
			for (int j = 0; j < id.length; j++) {
				if (j != 0)
					idLabel[i].setText(idLabel[i].getText() + " " + id[j]);
			}
		}

		for (int i = 0; i < name.length; i++) {
			idPanel[i] = new JPanel();
			idPanel[i].add(idLabel[i]);
		}

		if (search != null) {
			for (int i = 0; i < panel.length; i++) {
				if (!(name[i].contains(search) | article[i].contains(search) | idLabel[i].getText().contains(search))) {
					scrollCount--;
					continue;
				}
				panelMain.add(panel[i]);
				panelMain.add(idPanel[i]);
			}
		} else {
			for (int i = 0; i < panel.length; i++) {
				panelMain.add(panel[i]);
				panelMain.add(idPanel[i]);
			}
		}

		scrollCount += name.length;
		panelMain.setLayout(new GridLayout(scrollCount, 1));
		scroll = new JScrollPane(panelMain);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		this.add(scroll);
		this.invalidate();
		this.validate();
		this.repaint();
		if (loopSw == false)
			splashDestroy();
		return true;
	}

	public boolean addFriendsRequest(String id, String message) {
		try {
			URL url = null;
			String encodeMessage = URLEncoder.encode(message, "UTF-8");
			url = new URL(TalkFriendParseModule.getAddFriendsUrl("9414065", "7cb27443127e05841cbdb0da2963c1d8")
					+ encodeMessage + "&target_id=" + id);
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateScreen(String search, String timeMessage) {
		while (!(time[time.length - 1].equals(timeMessage))) {
			if (time[time.length - 1].contains("시간전") && timeMessage.contains("분전"))
				break;
			loadText.setText("불러오는중 ... " + time[time.length - 1] + " / " + name[name.length - 1]);
			module = new TalkFriendParseModule(timeId[timeId.length - 1], "9414065",
					"7cb27443127e05841cbdb0da2963c1d8");
			result = module.getList();

			name = result[0];
			age = result[1];
			time = result[2];
			article = result[3];
			image = result[4];
			timeId = result[5];
			userId = result[6];

			idList.addAll(Arrays.asList(userId));

			textLabel = new JLabel[name.length];
			idLabel = new JLabel[name.length];

			panel = new JPanel[name.length];

			idPanel = new JPanel[name.length];

			imageLabel = new JLabel[image.length];
			imageBuffer = null;

			addButton = new JButton[name.length];

			for (int i = 0; i < name.length; i++) {
				addButton[i] = new JButton("친구 추가 요청" + " (" + userId[i] + ")");
				addButton[i].addActionListener(this);
				textLabel[i] = new JLabel("<html>" + name[i] + " / " + age[i] + " / " + time[i] + " / " + "<br/>"
						+ article[i] + "</html>");
				textLabel[i].setFont(new Font("나눔고딕", Font.PLAIN, 14));
				try {
					URL url = new URL(image[i]);
					imageBuffer = ImageIO.read(url);
					imageLabel[i] = new JLabel(new ImageIcon(imageBuffer));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			for (int i = 0; i < panel.length; i++) {
				panel[i] = new JPanel();
				panel[i].add(imageLabel[i]);
				panel[i].add(textLabel[i]);
				panel[i].add(addButton[i]);
			}

			for (int i = 0; i < name.length; i++) {
				id = module.getUserInfo(userId[i]);
				idLabel[i] = new JLabel();
				for (int j = 0; j < id.length; j++) {
					if (j != 0)
						idLabel[i].setText(idLabel[i].getText() + " " + id[j]);
				}
			}

			for (int i = 0; i < name.length; i++) {
				idPanel[i] = new JPanel();
				idPanel[i].add(idLabel[i]);
			}

			if (search != null) {
				for (int i = 0; i < panel.length; i++) {
					if (!(name[i].contains(search) | article[i].contains(search)
							| idLabel[i].getText().contains(search))) {
						scrollCount--;
						continue;
					}
					panelMain.add(panel[i]);
					panelMain.add(idPanel[i]);
				}
			} else {
				for (int i = 0; i < panel.length; i++) {
					panelMain.add(panel[i]);
					panelMain.add(idPanel[i]);
				}
			}

			scrollCount += name.length;
			panelMain.setLayout(new GridLayout(scrollCount, 1));
			this.invalidate();
			this.validate();
			this.repaint();
		}
		splashDestroy();
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int i = 0;
		target = e.getActionCommand();
		target = target.substring(target.indexOf("(") + 1, target.length() - 1);
		while (true) {
			if (target.equals(idList.get(i))) {
				addFriendsRequest(idList.get(i), null);
				break;
			}
			i++;
		}
	}
}