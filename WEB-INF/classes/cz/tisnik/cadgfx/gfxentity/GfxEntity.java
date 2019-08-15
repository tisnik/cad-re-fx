package cz.tisnik.cadgfx.gfxentity;

import java.awt.Graphics2D;

public interface GfxEntity
{
    public void draw(Graphics2D gc);
    public GfxEntityAttribute getAttribute();
}
