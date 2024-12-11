import wholeCG.solution


class Component:
    def __init__(self, component_data):
        self.children = [Component(child) for child in component_data.get('children', [])]
        self.cg = component_data.get('cg', {})
        self.position = component_data.get('position', {})
        self.componentName = component_data.get('componentName', '')
        self.instanceCount = component_data.get('instanceCount', 0)
        self.allInstanceOffsets = component_data.get('allInstanceOffsets', [])
        self.allInstanceAngles = component_data.get('allInstanceAngles', [])
        self.instanceLocations = component_data.get('instanceLocations', [])
        self.isMotorMount = component_data.get('isMotorMount', bool)
        self.cMx = component_data.get('cMx', 0.0)
        self.motorXPosition = component_data.get('motorXPosition', 0.0)
        self.mountXPosition = component_data.get('mountXPosition', 0.0)
        self.eachMass = component_data.get('eachMass', 0.0)

    def __repr__(self):
        return (f"Component(name={self.componentName}, cg={self.cg}, position={self.position}, "
                f"instanceCount={self.instanceCount}, allInstanceOffsets={self.allInstanceOffsets}, "
                f"instanceLocations={self.instanceLocations}, isMotorMount={self.isMotorMount}, "
                f"cMx={self.cMx}, motorXPosition={self.motorXPosition}, mountXPosition={self.mountXPosition},"
                f" eachMass={self.eachMass},allInstanceAngles={self.allInstanceAngles}, children={self.children})")


def calculateCG(param):
    root_component = Component(param['myComponent'])
    return wholeCG.solution.calculateCG(root_component)
