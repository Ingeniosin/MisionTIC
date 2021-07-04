class Sucursal():
    def __init__(self, id):
        self.id = id
        self.medicamentos = requerirNumero()
        self.medicamentosOld = self.medicamentos

    def printInfo(self):
        percent = (self.medicamentosOld - self.medicamentos) / \
            self.medicamentosOld
        print(f"{self.id} {percent:.2%}")

    def printMaxOrMin(self):
        print(f"{self.id} {self.medicamentos}")


class Paciente():
    def __init__(self, sucursal, estado, glucosa):
        self.sucursal = sucursal
        self.estado = estado
        self.glucosa = glucosa
        self.restarMedicamento()
        pacientes.append(self)

    def getCantidadMedicamentos(self, g):
        if self.estado == estados[0]:
            return 2 if g < 0.07 else (0 if g < 0.1 else (3 if g < 0.125 else 6))
        else:
            return 0 if g < 0.14 else (6 if g < 0.2 else 12)

    def restarMedicamento(self):
        self.sucursal.medicamentos -= self.getCantidadMedicamentos(
            self.glucosa)


def requerirNumero(limit=0):
    numero = getInp()
    while (not numero.isdigit()) or int(numero) <= limit:
        numero = getInp()
    return int(numero)


def initSucursales():
    global Nsucursales
    for i in range(1, Nsucursales+1):
        sucursales.append(Sucursal(i))


def requerirPacientes():
    global cantidadDePaciente
    for i in range(1, cantidadDePaciente + 1):
        Nsucursal = requerirNumero() - 1
        sucursal = sucursales[Nsucursal] if Nsucursal < len(
            sucursales) else None
        estado = getInp().lower()
        glucosa = float(getInp())
        if estados.__contains__(estado) and sucursal is not None:
            Paciente(sucursal, estado, glucosa)
            continue
        global pacientesOmitidos
        pacientesOmitidos += 1


def terminarProceso():
    sucursalesSorted = sorted(sucursales, key=lambda sucursal: sucursal.medicamentos)
    sucursalesSorted[0].printMaxOrMin()
    sucursalesSorted[len(sucursalesSorted)-1].printMaxOrMin()
    for i in sucursales:
        i.printInfo()


def main():
    initSucursales()
    requerirPacientes()
    terminarProceso()


def getInp():
    if len(lastInput) == 0:
        inp = input()
        if " " in inp:
            lastInput.extend(inp.split(" "))
        else:
            lastInput.append(inp)
    inp = lastInput[0]
    lastInput.remove(inp)
    return inp


lastInput = []
Nsucursales = requerirNumero()
cantidadDePaciente = requerirNumero()
estados = ["ayunas", "posprandial"]
sucursales = []
pacientes = []
pacientesOmitidos = 0
main()
