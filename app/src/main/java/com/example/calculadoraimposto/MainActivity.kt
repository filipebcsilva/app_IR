package com.example.calculadoraimposto

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadoraimposto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calcularButton.setOnClickListener(this)
    }


    fun calcImposto(salario: Double,gastos: Double,dependentes: Int): Double{
        val deducaoDependente = dependentes * 189.59
        val deducao = if (gastos > 607.20) gastos else 607.20

        val base = salario - deducao - deducaoDependente

        val (aliquota, parcelaDeduzir) = when {
            base <= 2428.80 -> 0.0 to 0.0
            base <= 2826.65 -> 0.075 to 182.16
            base <= 3751.05 -> 0.15 to 394.16
            base <= 4664.68 -> 0.225 to 675.49
            else -> 0.275 to 908.73
        }

        var imposto = (base * aliquota) - parcelaDeduzir
        if (imposto < 0) imposto = 0.0

        imposto = when {
            salario <= 5000.0 -> 0.0
            salario <= 7350.0 -> {
                val reducao = 978.62 - (0.133145 * salario)
                maxOf(0.0, imposto - reducao)
            }
            else -> imposto
        }

        return imposto
    }

    override fun onClick(view: View) {
        if (view.id == binding.calcularButton.id) {
            val salario = binding.salarioEdit.text.toString().toDoubleOrNull() ?: 0.0
            val gastos = binding.gastosEdit.text.toString().toDoubleOrNull() ?: 0.0
            val dependentes = binding.dependenteEdit.text.toString().toIntOrNull() ?: 0

            val imposto = calcImposto(salario,gastos,dependentes)
            binding.impostoView.text = String.format("%.2f",imposto)
        }
    }

}