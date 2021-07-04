class Medicamento:

    def __init__(self, idMedicamento, cantidad):
        self.id = idMedicamento
        self.cantidad = cantidad
        self.cantidadDados = 0


class Sucursal:

    def __init__(self, idSucursal, medicamentos):
        self.id = idSucursal
        self.medicamentos = sorted(medicamentos, key=lambda e: e.id)
        self.cantidadDadosAcumulados = 0
        self.pacientesEnSucursal = 0
        sucursales[idSucursal] = self

    def printInfo(self):
        ordenarLista = lambda reverse: sorted(sorted(self.medicamentos.copy(), key=lambda e: e.id, reverse=reverse), key=lambda e: e.cantidad)
        menor = ordenarLista(False)[0]
        mayor = ordenarLista(True)[len(self.medicamentos) - 1]
        print(f"{self.id}")
        print(f"{menor.id} {menor.cantidad}")
        print(f"{mayor.id} {mayor.cantidad}")
        print(f"{self.getMaxOrMinMed(False):.2f} {(self.cantidadDadosAcumulados / cantidadTipoDeMedicamento if cantidadTipoDeMedicamento != 0 else 0):.2f} {self.getMaxOrMinMed():.2f}")
        print(f"{(self.cantidadDadosAcumulados / self.pacientesEnSucursal if self.pacientesEnSucursal != 0 else 0):.2f}")

    def restarMedicamento(self, medicamento, cantidad):
        medicamento.cantidad -= cantidad
        medicamento.cantidadDados += cantidad
        self.cantidadDadosAcumulados += cantidad

    def getMaxOrMinMed(self, max=True):
        a = 0 if max else 10**100
        for medicamento in self.medicamentos:
            if a < medicamento.cantidadDados if max else a > medicamento.cantidadDados:
                a = medicamento.cantidadDados
        return a


class Paciente:

    def __init__(self, sucursal, estado, glucosa, medicamento, existenciasSolicitadas):
        self.sucursal = sucursal
        self.medicamento = medicamento
        self.existenciasSolicitadas = existenciasSolicitadas
        self.estado = estado
        self.glucosa = glucosa
        self.sucursal.pacientesEnSucursal += 1
        if requiereMedicamento(self.estado, self.glucosa):
            self.sucursal.restarMedicamento(self.medicamento, self.existenciasSolicitadas)


def requiereMedicamento(e, g):
    if e == estados[0]:
        return True if g < 0.07 else (False if g < 0.1 else (True if g < 0.125 else True))
    else:
        return False if g < 0.14 else (True if g < 0.2 else True)


def terminarProceso():
    for sucursal in sucursales.values():
        sucursal.printInfo()
    ordenarLista = lambda num, reverse: sorted(
        sorted(list(sucursales.copy().values()), key=lambda e: e.id, reverse=reverse),key=lambda e: e.medicamentos[num - 1].cantidadDados)
    menor = ordenarLista(1, False)[0]
    mayor = ordenarLista(1, True)[len(sucursales) - 1]
    print(f"{menor.id} {menor.medicamentos[0].cantidadDados}")
    print(f"{mayor.id} {mayor.medicamentos[0].cantidadDados}")


def requerirPacientes():
    for idPaciente in range(1, cantidadPacientes + 1):
        try:
            sucursal = sucursales.get(int(getInput()))
            numeroMedicamento = int(getInput())
            medicamento = sucursal.medicamentos[numeroMedicamento - 1] if sucursal is not None else None
            existenciasSolicitadas = int(getInput())
            estado = getInput().lower()
            glucosa = float(getInput())
            if sucursal is not None and medicamento is not None and existenciasSolicitadas >= 0 and estados.__contains__(estado):
                Paciente(sucursal, estado, glucosa, medicamento, existenciasSolicitadas)
        except Exception as e:
            print(f"{e}")


def initSucursales():
    for idSucursal in range(1, cantidadSucursales + 1):
        medicamentos = []
        for idMedicamento in range(1, cantidadTipoDeMedicamento + 1):
            medicamentos.append(Medicamento(idMedicamento, requerirNumero()))
        Sucursal(idSucursal, medicamentos)


def main():
    initSucursales()
    requerirPacientes()
    terminarProceso()


def requerirNumero(limit=0):
    numero = getInput()
    return requerirNumero(limit) if (not numero.isdigit()) or int(numero) <= limit else int(numero)


def getInput():
    if len(lastInput) == 0:
        inp = input()
        if " " in inp:
            lastInput.extend(inp.split(" "))
        else:
            return inp
    inp = lastInput[0]
    lastInput.remove(inp)
    return inp


lastInput = []
sucursales = {}
estados = ["ayunas", "posprandial"]
cantidadSucursales = requerirNumero()
cantidadTipoDeMedicamento = requerirNumero()
cantidadPacientes = requerirNumero()

main()
