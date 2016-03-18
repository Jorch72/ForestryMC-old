/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.greenhouse.logics;

import forestry.api.core.EnumCamouflageType;
import forestry.api.core.ICamouflagedBlock;
import forestry.api.greenhouse.DefaultGreenhouseLogic;
import forestry.api.greenhouse.EnumGreenhouseChangeType;
import forestry.api.greenhouse.GreenhouseManager;
import forestry.api.greenhouse.IGreenhouseClimaLogic;
import forestry.api.multiblock.IGreenhouseController;
import forestry.api.multiblock.IMultiblockComponent;
import forestry.core.utils.CamouflageUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GreenhouseEffect extends DefaultGreenhouseLogic implements IGreenhouseClimaLogic {

	private float lightTransmittance;
	private int workTimer;
	
	public GreenhouseEffect(IGreenhouseController controller) {
		super(controller, "GreenhouseEffect");
		
	}
	
	@Override
	public void onWork() {
		if(controller == null || !controller.isAssembled()){
			return;
		}
		if(controller.getWorld().isDaytime()){
			controller.addTemperatureChange(lightTransmittance / 100, 0F, 2.5F);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("workTimer", workTimer);
		nbt.setFloat("lightTransmittance", lightTransmittance);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		workTimer = nbt.getInteger("workTimer");
		lightTransmittance = nbt.getFloat("lightTransmittance");
	}

	@Override
	public void onChange(EnumGreenhouseChangeType type, Object event) {
		if(type == EnumGreenhouseChangeType.CAMOUFLAGE){
			if(controller == null || !controller.isAssembled()) {
				return;
			}
			float lightTransmittance = 0F;
			int i = 0;
			
			World world = controller.getWorld();
			for(IMultiblockComponent component : controller.getComponents()){
				if(component instanceof ICamouflagedBlock){
					ICamouflagedBlock block = (ICamouflagedBlock) component;
					if(block.getCamouflageType() == EnumCamouflageType.GLASS){
						if(world.canBlockSeeSky(component.getCoordinates())){
							ItemStack camouflageStack = CamouflageUtil.getCamouflageBlock(world, component.getCoordinates());
							float camouflageLightTransmittance = GreenhouseManager.greenhouseAccess.getGreenhouseGlassLightTransmittance(camouflageStack);
							
							if(camouflageLightTransmittance < 1 && camouflageLightTransmittance > 0){
								lightTransmittance=+ camouflageLightTransmittance;
								i++;
							}
						}
					}
				}
			}
			if(i != 0){
				this.lightTransmittance = lightTransmittance / i;
			}
		}
	}

}