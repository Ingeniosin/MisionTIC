from math import sqrt

class Sucursal:

    def __init__(self, idSucursal):
        self.id = idSucursal
        self.pacientes = []
        sucursales[idSucursal] = self

class Paciente:

    def __init__(self, first_name, last_name, gender, city_name, department_name, id_branch, medicine_type, medicine_quantity, fasting, glucose):
        self.first_name = first_name
        self.last_name = last_name
        self.gender = gender
        self.city_name = city_name
        self.department_name = department_name
        self.id_branch = id_branch
        self.medicine_type = medicine_type
        self.medicine_quantity = medicine_quantity
        self.fasting = fasting
        self.glucose = glucose
        getOrCreateSucursal(id_branch).pacientes.append(self)

    def requiereMedicamento(self):
        if self.fasting == estados[0]:
            return True if self.glucose < 0.07 else (False if self.glucose < 0.1 else (True if self.glucose < 0.125 else True))
        else:
            return False if self.glucose < 0.14 else (True if self.glucose < 0.2 else True)

    def information(self, scheduled=False):
        return f"{self.medicine_quantity} {self.first_name} {self.last_name} {self.gender} {self.medicine_type}" if scheduled else self.medicine_quantity

def inflateList():
    file = open("data.csv", "r+")
    cantidadLineas = sum(1 for _ in open('data.csv'))
    for i in range(0, cantidadLineas):
        line = file.readline()
        split = line.split(",")
        try:
            Paciente(split[0], split[1], split[2], split[3], split[4], int(split[5]), int(split[6]), int(split[7]), split[8], float(split[9]))
        except Exception:
            continue
    file.close()

def getOrCreateSucursal(idSucursal):
    return sucursales.get(idSucursal) if sucursales.__contains__(idSucursal) else Sucursal(idSucursal)

def printInformation(pacientes, s=""):
    cantidadGenero = lambda genero: sum(1 for _ in list(filter(lambda x: x.gender == genero, pacientes)))
    cantidadPacientes = len(pacientes)
    totalMedicamentos = sum(i.medicine_quantity for i in pacientes)
    promedioMedicamentos = totalMedicamentos / cantidadPacientes
    desviacionEstandar = sqrt(sum((i.medicine_quantity-promedioMedicamentos)**2 for i in pacientes)/(len(pacientes)-1))
    maxAndMin = lambda x=False: list(sorted(pacientes.copy(), key=lambda i: i.medicine_quantity, reverse=x))
    scheduled = s != ""
    print(f"{s}patients")
    print(f"male", cantidadGenero("m"))
    print(f"female", cantidadGenero("f"))
    print(f"total", cantidadPacientes)
    print(f"{s}medicine quantity")
    print(f"mean {promedioMedicamentos:.2f}")
    print(f"std {desviacionEstandar:.2f}")
    print(f"min {maxAndMin()[0].information(scheduled)}")
    print(f"max {maxAndMin(True)[0].information(scheduled)}")
    print(f"total {totalMedicamentos}")

def performProcess(sucursal):
    pacientes = getOrCreateSucursal(sucursal).pacientes
    print(f"{sucursal} {pacientes[0].city_name} {pacientes[0].department_name}")
    printInformation(pacientes)
    printInformation(list(filter(lambda x: x.requiereMedicamento(), pacientes)), "scheduled ")


sucursales = {}
estados = ["ayunas", "posprandial"]
inflateList()
listInput = list(int(i) for i in input().split(" "))
listInput.sort()
for i in listInput:
    performProcess(i)
