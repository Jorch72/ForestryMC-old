package forestry.core.utils;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import forestry.core.interfaces.IOwnable;

public class PlayerUtil {

	//TODO: use null everywhere instead of an emptyUUID
	private static final UUID emptyUUID = new UUID(0, 0);

	public static boolean isSameGameProfile(GameProfile player1, GameProfile player2) {
		if (player1 == null || player2 == null) {
			return false;
		}

		UUID id1 = player1.getId();
		UUID id2 = player2.getId();
		if (id1 != null && id2 != null && !id1.equals(emptyUUID) && !id2.equals(emptyUUID)) {
			return id1.equals(id2);
		}

		return player1.getName() != null && player1.getName().equals(player2.getName());
	}

	public static String getOwnerName(IOwnable ownable) {
		GameProfile profile = ownable.getOwnerProfile();
		if (profile == null) {
			return StringUtil.localize("gui.derelict");
		} else {
			return profile.getName();
		}
	}

}
