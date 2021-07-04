def performResult(state, glucosa):
    if state == options[0]:
        return "hipoglusemia" if glucosa < 0.07 else ("normal" if glucosa < 0.1 else ("elevado" if glucosa < 0.125 else "diabetes"))
    else:
        return "normal" if glucosa < 0.14 else ("elevado" if glucosa < 0.2 else "diabetes")


options = ["ayunas", "posprandial"]
userState = input("Ingresa tu estado, Ayunas o Posprandial: ").lower()
userGlucosa = float(input("Ingresa tu glucosa en sangre: "))
print("error en los datos" if not options.__contains__(userState) else performResult(userState, userGlucosa))
