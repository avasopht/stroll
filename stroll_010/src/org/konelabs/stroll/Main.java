package org.konelabs.stroll;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.konelabs.stroll.graphics.GBAGraphics;
import org.konelabs.stroll.graphics.GBAGraphicsCanvas;
import org.konelabs.stroll.gui.GuiManager;
import org.konelabs.stroll.gui.screens.TitleScreen;
import org.konelabs.stroll.system.Input;
import org.konelabs.stroll.system.KeyboardInput;

public class Main {
  public static void main(String argv[]) {
    GBAGraphicsCanvas canvas = new GBAGraphicsCanvas();
    Frame frame = new Frame("Stroll: on drugs");
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.addKeyListener(new KeyboardInput());
    frame.add(canvas);
    frame.setVisible(true);
    frame.pack();

    Input.initialize();
    GBAGraphics.getInstance().start();

    GuiManager gui = new GuiManager();
    gui.openScreen(new TitleScreen(gui));
    gui.monitor();
  }
}
