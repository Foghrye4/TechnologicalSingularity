package technological_singularity.client;

import java.util.function.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

/** Return 0 and 1 for U and V texture coordinates. */
public class DummyTextureAtlasSprite extends TextureAtlasSprite implements Function<ResourceLocation, TextureAtlasSprite> {

	public static DummyTextureAtlasSprite instance = new DummyTextureAtlasSprite();
	
	public DummyTextureAtlasSprite() {
		super("dummy");
	}

	@Override
	public float getMinU() {
		return 0.0f;
	}

	@Override
	public float getMaxU() {
		return 1.0f;
	}

	@Override
	public float getInterpolatedU(double u) {
		return (float) u / 16.0f;
	}

	@Override
	public float getMinV() {
		return 0.0f;
	}

	@Override
	public float getMaxV() {
		return 1.0f;
	}

	@Override
	public float getInterpolatedV(double v) {
		return (float) v / 16.0f;
	}

	@Override
	public TextureAtlasSprite apply(ResourceLocation t) {
		return instance;
	}
}
