import kr.pe.dreamer.TalkFriendSwingModule;

public class Main {
	public static void main(String[] args) {
		TalkFriendSwingModule module = new TalkFriendSwingModule("test");
		module.initializeScreen("전화", true);
		module.updateScreen("전화", "1시간전");
	}
}
