import kr.pe.dreamer.TalkFriendSwingModule;

public class Main {
	public static void main(String[] args) {
		TalkFriendSwingModule module = new TalkFriendSwingModule("test");
		module.initializeScreen("자동차", true);
		module.updateScreen("자동차", "2일전");
	}
}
