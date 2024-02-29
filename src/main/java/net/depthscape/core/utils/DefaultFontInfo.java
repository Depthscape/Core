package net.depthscape.core.utils;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public enum DefaultFontInfo {

	A('A', 6),
	a('a', 6),
	B('B', 6),
	b('b', 6),
	C('C', 6),
	c('c', 6),
	D('D', 6),
	d('d', 6),
	E('E', 6),
	e('e', 6),
	F('F', 6),
	f('f', 6),
	G('G', 6),
	g('g', 6),
	H('H', 6),
	h('h', 6),
	I('I', 4),
	i('i', 1),
	J('J', 6),
	j('j', 6),
	K('K', 6),
	k('k', 5),
	L('L', 6),
	l('l', 3),
	M('M', 6),
	m('m', 6),
	N('N', 6),
	n('n', 6),
	O('O', 6),
	o('o', 6),
	P('P', 6),
	p('p', 6),
	Q('Q', 6),
	q('q', 6),
	R('R', 6),
	r('r', 6),
	S('S', 6),
	s('s', 6),
	T('T', 6),
	t('t', 4),
	U('U', 6),
	u('u', 6),
	V('V', 6),
	v('v', 6),
	W('W', 6),
	w('w', 6),
	X('X', 6),
	x('x', 6),
	Y('Y', 6),
	y('y', 6),
	Z('Z', 6),
	z('z', 6),
	NUM_1('1', 6),
	NUM_2('2', 6),
	NUM_3('3', 6),
	NUM_4('4', 6),
	NUM_5('5', 6),
	NUM_6('6', 6),
	NUM_7('7', 6),
	NUM_8('8', 6),
	NUM_9('9', 6),
	NUM_0('0', 6),
	EXCLAMATION_POINT('!', 2),
	AT_SYMBOL('@', 7),
	NUM_SIGN('#', 6),
	DOLLAR_SIGN('$', 6),
	PERCENT('%', 6),
	UP_ARROW('^', 6),
	AMPERSAND('&', 6),
	ASTERISK('*', 4),
	LEFT_PARENTHESIS('(', 4),
	RIGHT_PERENTHESIS(')', 4),
	MINUS('-', 6),
	UNDERSCORE('_', 6),
	PLUS_SIGN('+', 6),
	EQUALS_SIGN('=', 6),
	LEFT_CURL_BRACE('{', 4),
	RIGHT_CURL_BRACE('}', 4),
	LEFT_BRACKET('[', 4),
	RIGHT_BRACKET(']', 4),
	COLON(':', 2),
	SEMI_COLON(';', 2),
	DOUBLE_QUOTE('"', 4),
	SINGLE_QUOTE('\'', 2),
	LEFT_ARROW('<', 5),
	RIGHT_ARROW('>', 5),
	QUESTION_MARK('?', 6),
	SLASH('/', 6),
	BACK_SLASH('\\', 6),
	LINE('|', 2),
	TILDE('~', 7),
	TICK('`', 3),
	PERIOD('.', 2),
	COMMA(',', 2),
	SPACE(' ', 6),
	OTHER_A('ᴀ', 4),
	OTHER_B('ʙ', 4),
	OTHER_C('ᴄ', 4),
	OTHER_D('ᴅ', 4),
	OTHER_E('ᴇ', 4),
	OTHER_F('ꜰ', 4),
	OTHER_G('ɢ', 4),
	OTHER_H('ʜ', 4),
	OTHER_I('ɪ', 2),
	OTHER_J('ᴊ', 4),
	OTHER_K('ᴋ', 4),
	OTHER_L('ʟ', 4),
	OTHER_M('ᴍ', 4),
	OTHER_N('ɴ', 4),
	OTHER_O('ᴏ', 4),
	OTHER_P('ᴘ', 4),
	OTHER_Q('ǫ', 4),
	OTHER_R('ʀ', 4),
	OTHER_S('ꜱ', 4),
	OTHER_T('ᴛ', 4),
	OTHER_U('ᴜ', 4),
	OTHER_V('ᴠ', 4),
	OTHER_W('ᴡ', 4),
	OTHER_X('x', 4),
	OTHER_Y('ʏ', 4),
	OTHER_Z('ᴢ', 4),
	DEFAULT('a', 6);


	private final char character;
	private final int length;

	DefaultFontInfo(char character, int length) {
		this.character = character;
		this.length = length;
	}

	public int getBoldLength() {
		if (this == DefaultFontInfo.SPACE) return this.getLength();
		return this.length + 1;
	}

	public static DefaultFontInfo getDefaultFontInfo(char c) {
		for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
			if (dFI.getCharacter() == c) return dFI;
		}
		return null;
	}

	public static int getStringLength(String str) {

		// strip color
		str = ChatColor.stripColor(str);

		int length = 0;
		boolean isBold = false;
		for (char c : str.toCharArray()) {
			if (c == '§' && !isBold) {
				isBold = true;
				continue;
			}
			DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
			if (dFI != null) {
				length += isBold ? dFI.getBoldLength() : dFI.getLength();
				isBold = false;
				continue;
			}
			CustomFontCharacter cFC = CustomFontCharacter.getCharacter(c);
			if (cFC != null) {
				length = cFC.getLength();
				isBold = false;
			}

		}
		return length;
	}

	public String translateStringToUnicode(String string) {
		StringBuilder stringBuilder = new StringBuilder();
		for (char c : string.toLowerCase().toCharArray()) {
			for (DefaultFontInfo dfi : DefaultFontInfo.values()) {
				if (!dfi.name().startsWith("OTHER_")) continue;
				String character = dfi.name().substring(6).toLowerCase();
				if (c == character.charAt(0)) {
					stringBuilder.append(dfi.getCharacter());
					break;
				}
			}
		}
		return stringBuilder.toString();
	}
}