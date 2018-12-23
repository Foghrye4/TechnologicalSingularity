package shadersmod.client;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.ARBShaderObjects;

public class Shaders
{
  public static boolean isShaderPackInitialized = false;
  public static String glVersionString;
  public static String glVendorString;
  public static String glRendererString;
  public static boolean hasGlGenMipmap = false;
  public static boolean hasForge = false;
  public static int numberResetDisplayList = 0;
  public static boolean needResetModels = false;
  
  public static int renderDisplayWidth = 0;
  public static int renderDisplayHeight = 0;
  public static int renderWidth = 0;
  public static int renderHeight = 0;
  
  public static boolean isRenderingWorld = false;
  public static boolean isRenderingSky = false;
  public static boolean isCompositeRendered = false;
  public static boolean isRenderingDfb = false;
  public static boolean isShadowPass = false;
  
  public static boolean isSleeping;
  public static boolean isRenderingFirstPersonHand;
  public static boolean isHandRenderedMain;
  public static boolean isHandRenderedOff;
  public static boolean skipRenderHandMain;
  public static boolean skipRenderHandOff;
  public static boolean renderItemKeepDepthMask = false;
  
  public static boolean itemToRenderMainTranslucent = false;
  public static boolean itemToRenderOffTranslucent = false;
  
  public static float[] sunPosition = new float[4];
  public static float[] moonPosition = new float[4];
  public static float[] shadowLightPosition = new float[4];
  public static float[] upPosition = new float[4];
  public static float[] shadowLightPositionVector = new float[4];
  
  public static float[] upPosModelView = { 0.0F, 100.0F, 0.0F, 0.0F };
  public static float[] sunPosModelView = { 0.0F, 100.0F, 0.0F, 0.0F };
  public static float[] moonPosModelView = { 0.0F, -100.0F, 0.0F, 0.0F };
  public static float[] tempMat = new float[16];
  
  public static float clearColorR;
  
  public static float clearColorG;
  
  public static float clearColorB;
  public static float skyColorR;
  public static float skyColorG;
  public static float skyColorB;
  public static long worldTime = 0L;
  public static long lastWorldTime = 0L;
  public static long diffWorldTime = 0L;
  public static float celestialAngle = 0.0F;
  public static float sunAngle = 0.0F;
  public static float shadowAngle = 0.0F;
  public static int moonPhase = 0;
  
  public static long systemTime = 0L;
  public static long lastSystemTime = 0L;
  public static long diffSystemTime = 0L;
  
  public static int frameCounter = 0;
  public static float frameTime = 0.0F;
  public static float frameTimeCounter = 0.0F;
  
  public static int systemTimeInt32 = 0;
  
  public static float rainStrength = 0.0F;
  public static float wetness = 0.0F;
  public static float wetnessHalfLife = 600.0F;
  public static float drynessHalfLife = 200.0F;
  public static float eyeBrightnessHalflife = 10.0F;
  public static boolean usewetness = false;
  public static int isEyeInWater = 0;
  public static int eyeBrightness = 0;
  public static float eyeBrightnessFadeX = 0.0F;
  public static float eyeBrightnessFadeY = 0.0F;
  public static float eyePosY = 0.0F;
  public static float centerDepth = 0.0F;
  public static float centerDepthSmooth = 0.0F;
  public static float centerDepthSmoothHalflife = 1.0F;
  public static boolean centerDepthSmoothEnabled = false;
  public static int superSamplingLevel = 1;
  public static float nightVision = 0.0F;
  public static float blindness = 0.0F;
  
  public static boolean updateChunksErrorRecorded = false;
  
  public static boolean lightmapEnabled = false;
  public static boolean fogEnabled = true;
  
  public static int entityAttrib = 10;
  public static int midTexCoordAttrib = 11;
  public static int tangentAttrib = 12;
  public static boolean useEntityAttrib = false;
  public static boolean useMidTexCoordAttrib = false;
  public static boolean useMultiTexCoord3Attrib = false;
  public static boolean useTangentAttrib = false;
  public static boolean progUseEntityAttrib = false;
  public static boolean progUseMidTexCoordAttrib = false;
  public static boolean progUseTangentAttrib = false;
  
  public static int atlasSizeX = 0; public static int atlasSizeY = 0;
  
  public static double previousCameraPositionX;
  public static double previousCameraPositionY;
  public static double previousCameraPositionZ;
  public static double cameraPositionX;
  public static double cameraPositionY;
  public static double cameraPositionZ;
  public static int shadowPassInterval = 0;
  public static boolean needResizeShadow = false;
  public static int shadowMapWidth = 1024;
  public static int shadowMapHeight = 1024;
  public static int spShadowMapWidth = 1024;
  public static int spShadowMapHeight = 1024;
  public static float shadowMapFOV = 90.0F;
  public static float shadowMapHalfPlane = 160.0F;
  public static boolean shadowMapIsOrtho = true;
  public static float shadowDistanceRenderMul = -1.0F;
  
  public static int shadowPassCounter = 0;
  
  public static int preShadowPassThirdPersonView;
  
  public static boolean shouldSkipDefaultShadow = false;
  
  public static boolean waterShadowEnabled = false;
  
  public static final int MaxDrawBuffers = 8;
  
  public static final int MaxColorBuffers = 8;
  
  public static final int MaxDepthBuffers = 3;
  public static final int MaxShadowColorBuffers = 8;
  public static final int MaxShadowDepthBuffers = 2;
  public static int usedColorBuffers = 0;
  public static int usedDepthBuffers = 0;
  public static int usedShadowColorBuffers = 0;
  public static int usedShadowDepthBuffers = 0;
  public static int usedColorAttachs = 0;
  public static int usedDrawBuffers = 0;
  
  public static int dfb = 0;
  public static int sfb = 0;
  
  public static int[] gbuffersFormat = new int[8];
  public static boolean[] gbuffersClear = new boolean[8];
  


  public static int activeProgram = 0;
  
  public static final int ProgramNone = 0;
  
  public static final int ProgramBasic = 1;
  
  public static final int ProgramTextured = 2;
  
  public static final int ProgramTexturedLit = 3;
  
  public static final int ProgramSkyBasic = 4;
  
  public static final int ProgramSkyTextured = 5;
  public static final int ProgramClouds = 6;
  public static final int ProgramTerrain = 7;
  public static final int ProgramTerrainSolid = 8;
  public static final int ProgramTerrainCutoutMip = 9;
  public static final int ProgramTerrainCutout = 10;
  public static final int ProgramDamagedBlock = 11;
  public static final int ProgramWater = 12;
  public static final int ProgramBlock = 13;
  public static final int ProgramBeaconBeam = 14;
  public static final int ProgramItem = 15;
  public static final int ProgramEntities = 16;
  public static final int ProgramArmorGlint = 17;
  public static final int ProgramSpiderEyes = 18;
  public static final int ProgramHand = 19;
  public static final int ProgramWeather = 20;
  public static final int ProgramComposite = 21;
  public static final int ProgramComposite1 = 22;
  public static final int ProgramComposite2 = 23;
  public static final int ProgramComposite3 = 24;
  public static final int ProgramComposite4 = 25;
  public static final int ProgramComposite5 = 26;
  public static final int ProgramComposite6 = 27;
  public static final int ProgramComposite7 = 28;
  public static final int ProgramFinal = 29;
  public static final int ProgramShadow = 30;
  public static final int ProgramShadowSolid = 31;
  public static final int ProgramShadowCutout = 32;
  public static final int ProgramDeferred = 33;
  public static final int ProgramDeferred1 = 34;
  public static final int ProgramDeferred2 = 35;
  public static final int ProgramDeferred3 = 36;
  public static final int ProgramDeferred4 = 37;
  public static final int ProgramDeferred5 = 38;
  public static final int ProgramDeferred6 = 39;
  public static final int ProgramDeferred7 = 40;
  public static final int ProgramHandWater = 41;
  public static final int ProgramDeferredLast = 42;
  public static final int ProgramCompositeLast = 43;
  public static final int ProgramCount = 44;
  public static final int MaxCompositePasses = 8;
  public static final int MaxDeferredPasses = 8;
  public static final String[] programNames = { "", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_skybasic", "gbuffers_skytextured", "gbuffers_clouds", "gbuffers_terrain", "gbuffers_terrain_solid", "gbuffers_terrain_cutout_mip", "gbuffers_terrain_cutout", "gbuffers_damagedblock", "gbuffers_water", "gbuffers_block", "gbuffers_beaconbeam", "gbuffers_item", "gbuffers_entities", "gbuffers_armor_glint", "gbuffers_spidereyes", "gbuffers_hand", "gbuffers_weather", "composite", "composite1", "composite2", "composite3", "composite4", "composite5", "composite6", "composite7", "final", "shadow", "shadow_solid", "shadow_cutout", "deferred", "deferred1", "deferred2", "deferred3", "deferred4", "deferred5", "deferred6", "deferred7", "gbuffers_hand_water", "deferred_last", "composite_last" };
  













































  public static final int[] programBackups = { 0, 0, 1, 2, 1, 2, 2, 3, 7, 7, 7, 7, 7, 7, 2, 3, 3, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0, 0 };
  














































  public static int[] programsID = new int[44];
  public static int[] programsRef = new int[44];
  public static int programIDCopyDepth = 0;
  public static boolean hasDeferredPrograms = false;
  
  public static String[] programsDrawBufSettings = new String[44];
  public static String newDrawBufSetting = null;
  
  public static String[] programsColorAtmSettings = new String[44];
  public static String newColorAtmSetting = null;
  public static String activeColorAtmSettings = null;
  
  public static int[] programsCompositeMipmapSetting = new int[44];
  public static int newCompositeMipmapSetting = 0;
  public static int activeCompositeMipmapSetting = 0;
  
  public static boolean normalMapEnabled = false;
  public static boolean[] shadowHardwareFilteringEnabled = new boolean[2];
  public static boolean[] shadowMipmapEnabled = new boolean[2];
  public static boolean[] shadowFilterNearest = new boolean[2];
  public static boolean[] shadowColorMipmapEnabled = new boolean[8];
  public static boolean[] shadowColorFilterNearest = new boolean[8];
  

  public static boolean configTweakBlockDamage = false;
  public static boolean configCloudShadow = false;
  
  public static float configHandDepthMul = 0.125F;
  public static float configRenderResMul = 1.0F;
  public static float configShadowResMul = 1.0F;
  public static int configTexMinFilB = 0;
  public static int configTexMinFilN = 0;
  public static int configTexMinFilS = 0;
  public static int configTexMagFilB = 0;
  public static int configTexMagFilN = 0;
  public static int configTexMagFilS = 0;
  public static boolean configShadowClipFrustrum = true;
  public static boolean configNormalMap = true;
  public static boolean configSpecularMap = true;
  public static int configAntialiasingLevel = 0;
  
  public static final int texMinFilRange = 3;
  
  public static final int texMagFilRange = 2;
  public static final String[] texMinFilDesc = { "Nearest", "Nearest-Nearest", "Nearest-Linear" };
  public static final String[] texMagFilDesc = { "Nearest", "Linear" };
  public static final int[] texMinFilValue = { 9728, 9984, 9986 };
  public static final int[] texMagFilValue = { 9728, 9729 };
  
  public static boolean shaderPackLoaded = false;
  
  public static final int STAGE_GBUFFERS = 0;
  public static final int STAGE_COMPOSITE = 1;
  public static final int STAGE_DEFERRED = 2;
  public static final String[] STAGE_NAMES = { "gbuffers", "composite", "deferred" };
  
  public static final boolean enableShadersOption = true;
  public static final boolean enableShadersDebug = true;
  public static final boolean saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
  
  public static float blockLightLevel05 = 0.5F;
  public static float blockLightLevel06 = 0.6F;
  public static float blockLightLevel08 = 0.8F;
  

  public static float aoLevel = -1.0F;
  
  public static float sunPathRotation = 0.0F;
  public static float shadowAngleInterval = 0.0F;
  public static int fogMode = 0;
  public static float fogColorR;
  public static float fogColorG; public static float fogColorB; public static float shadowIntervalSize = 2.0F;
  public static int terrainIconSize = 16;
  public static int[] terrainTextureSize = new int[2];
  
  public static boolean noiseTextureEnabled = false;
  public static int noiseTextureResolution = 256;
  
  public static final int[] dfbColorTexturesA = new int[16];
  public static final int[] colorTexturesToggle = new int[8];
  public static final int[] colorTextureTextureImageUnit = { 0, 1, 2, 3, 7, 8, 9, 10 };
  public static final boolean[][] programsToggleColorTextures = new boolean[44][8];
  

  public static final int bigBufferSize = 2548;
  
  public static void setProgramUniform1i(String name, int x)
  {
  }
  
  public static void setProgramUniform2i(String name, int x, int y)
  {
  }
  
  public static void setProgramUniform1f(String name, float x)
  {
  }
  
  public static void setProgramUniform3f(String name, float x, float y, float z)
  {
  }
  
  public static void setProgramUniformMatrix4ARB(String name, boolean transpose, FloatBuffer matrix)
  {
  }
  
  public Shaders() {}
  
  public static void checkOptifine() {}
  
  public static void endFPOverlay() {}
}
