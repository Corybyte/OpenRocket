import step56.solution


def parse_cp(cp_dict):
    return (cp_dict['x'], cp_dict['y'], cp_dict['z'], cp_dict['weight'], cp_dict['length'])


def calculateCP(param):
    components = [WholeCpDTO(component['componentName'], parse_cp(component['cp']), component['rocketComponentCalc'])
                  for component in param['list']]

    cp = step56.solution.calculateCP(components)
    return cp[0]




class WholeCpDTO:
    def __init__(self, component_name, cp, rocket_component_calc, w=None):
        self.component_name = component_name
        self.cp = cp
        self.rocket_component_calc = rocket_component_calc

    def __repr__(self):
        return f"WholeCpDTO(component_name='{self.component_name}', cp={self.cp}, rocket_component_calc={self.rocket_component_calc})"

class WholeCpRequest:
    def __init__(self, components):
        self.components = components

    def __repr__(self):
        return f"WholeCpRequest(components={self.components})"
