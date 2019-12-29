package com.github.gilday.darkmode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.gilday.darkmode.DarkModeSyncThemes.State;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link DarkModeSyncThemes}. */
final class DarkModeSyncThemesTest {

  /**
   * Verifies that the {@link DarkModeSyncThemes.State} class may be serialized and de-serialized
   * using the {@link XmlSerializer}.
   */
  @Test
  void state_serialization_test() {
    final State expected = new State();
    final Element element = XmlSerializer.serialize(expected);
    final State actual = XmlSerializer.deserialize(element, State.class);

    assertEquals(expected, actual);
  }
}
