//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import com.google.common.collect.Lists;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import java.io.File;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ResourcePackRepository.class })
public class MixinResourcePackRepository
{
    @Shadow
    @Final
    private File dirServerResourcepacks;
    @Shadow
    @Final
    private static Logger logger;
    
    @Overwrite
    private void deleteOldServerResourcesPacks() {
        try {
            final List<File> lvt_1_1_ = (List<File>)Lists.newArrayList((Iterable)FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, (IOFileFilter)null));
            Collections.sort(lvt_1_1_, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            int lvt_2_1_ = 0;
            for (final File lvt_4_1_ : lvt_1_1_) {
                if (lvt_2_1_++ >= 10) {
                    MixinResourcePackRepository.logger.info("Deleting old server resource pack " + lvt_4_1_.getName());
                    FileUtils.deleteQuietly(lvt_4_1_);
                }
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
