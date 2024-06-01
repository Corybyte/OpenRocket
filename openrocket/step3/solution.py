from common_helper import average

def calculateCG(wettedCentroid: tuple[float, float, float, float], 
                tabCentroid: tuple[float, float, float, float], 
                filletCentroid: tuple[float, float, float, float],
                thickness: float,
                crossSectionRelativeVolume: float,
                materialDensity: float,
                filletMaterialDensity: float,
                finCount: int) -> float:
		wettedVolume = wettedCentroid[3] * thickness * crossSectionRelativeVolume
		finBulkMass = wettedVolume * materialDensity
		wettedCM = (wettedCentroid[0], wettedCentroid[1], wettedCentroid[2], finBulkMass)

		tabVolume = tabCentroid[3] * thickness
		tabMass = tabVolume * materialDensity
		tabCM = (tabCentroid[0], tabCentroid[1], tabCentroid[2], tabMass)
		
		filletVolume = filletCentroid[3]
		filletMass = filletVolume * filletMaterialDensity
		filletCM = (filletCentroid[0], filletCentroid[1], filletCentroid[2], filletMass)

		eachFinMass = finBulkMass + tabMass + filletMass
		eachFinCenterOfMass = average(average(wettedCM, tabCM), filletCM)
		eachFinCenterOfMass = (eachFinCenterOfMass[0], eachFinCenterOfMass[1], eachFinCenterOfMass[2], eachFinMass)
		
		# ^^ per fin
		# vv per component

		# set y coordinate: rotate around parent, if single fin; otherwise multiple fins will average out to zero
		centerOfMass = (eachFinCenterOfMass[0], 0, eachFinCenterOfMass[2], eachFinMass * finCount)
		return centerOfMass[0]