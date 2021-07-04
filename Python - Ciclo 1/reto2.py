def requerirUsuario():
    if revisarMedicamentos():
        terminarProceso()
        return
    global pacientes
    userState = input().lower()
    userGlucosa = float(input())
    if estados.__contains__(userState):
        medicamentoYDosis = definirMedicamentoYDosis(userState, userGlucosa)
        medicamento = medicamentoYDosis[0]
        if not medicamento == 0:
            dosis = medicamentoYDosis[1]
            discountCantidadMedicamento(medicamento - 1, dosis)
    pacientes += 1
    requerirUsuario()


def definirMedicamentoYDosis(userState, userGlucosa):
    if userState == estados[0]:
        return [2, 2] if userGlucosa < 0.07 else ([0, 0] if userGlucosa < 0.1 else ([1, 3] if userGlucosa < 0.125 else [1, 6]))
    else:
        return [0, 0] if userGlucosa < 0.14 else ([1, 6] if userGlucosa < 0.2 else [1, 12])


def revisarMedicamentos():
    for i in medicamentos:
        if i <= 0:
            return True
    return False


def discountCantidadMedicamento(medicamento, cantidad):
    medicamentosDados[medicamento] += 1
    medicamentos[medicamento] -= cantidad


def getCantidadMedicamento(medicamento):
    return medicamentos[medicamento]


def terminarProceso():
    print(f"{pacientes}")
    for i in medicamentosDados:
        percent = 0 if pacientes == 0 else i / pacientes
        print(f"{i} {percent:.2%}")
    return 1


def requerirExistencia(medicamentoId):
    return int(input())


def main():
    try:
        requerirUsuario()
    except Exception as e:
        print(f"ERROR: {e}")
        main()


medicamentos = [requerirExistencia(1), requerirExistencia(2)]
medicamentosDados = [0, 0]
estados = ["ayunas", "posprandial"]
pacientes = 0
main()
