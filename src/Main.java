import kr.pe.dreamer.TalkFriendSwingModule;

public class Main {
	public static void main(String[] args) {
		TalkFriendSwingModule module = new TalkFriendSwingModule("test");
		module.initializeScreen(null, true);
		module.updateScreen(null, "1분전");
	}
}
