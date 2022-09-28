/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    /**
     * === DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES ===
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     * === NÃO ALTERE NENHUMA VARIÁVEL OU NOME DE VALOR OU SEUS VALORES INICIAIS ===
     * Espera-se que qualquer coisa rotulada var em vez de val seja alterada nas funções, mas NÃO
     * alterar seus valores iniciais declarados aqui, isso pode fazer com que o aplicativo não funcione corretamente.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        //faz a ligação com a variavel lemonImage e o id da ImageView
        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()

        lemonImage!!.setOnClickListener {
            // TODO: call the method that handles the state when the image is clicked
            // TODO: chama o método que trata o estado quando a imagem é clicada
            clickLemonImage()
            //Log.d("Clicado", "Árvore");
            //Toast.makeText(this, "Tree cliked!", Toast.LENGTH_SHORT).show()
        }
        lemonImage!!.setOnLongClickListener {
            // TODO: replace 'false' with a call to the function that shows the squeeze count
            // TODO: substitua 'false' por uma chamada para a função que mostra a contagem de compressão
            // false
            //Toast.makeText(this, "Tree cliked!", Toast.LENGTH_SHORT).show()
            showSnackbar()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD / NÃO ALTERE ESTE MÉTODO ===
     * This method saves the state of the app if it is put in the background.
     * Este método salva o estado do aplicativo se for colocado em segundo plano.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    // Método que será chamado quando o usuário clicar na Imagem
    private fun clickLemonImage() {
        // Com base no lemonadeState atual, os dados podem ser alterados
        when (lemonadeState) {
            //(SELECT: muda para o estado SQUEEZE, define lemonSize (o número de espremidas
            // necessárias) chamando o método pick() e definindo squeezeCount (o número de
            // vezes que o usuário espremeu o limão) como 0.)
            SELECT -> {
                //se o estado é SELECT significa que ao clicar se quer espremer o limão
                //por isso seta-se o estado SQUEEZE
                lemonadeState = SQUEEZE
                // Determinando o tamanho da arvore pela função pick()
                // lemonTree é um objeto do tipo/classe LemonTree, seu valor é salvo na variavel tree
                val tree: LemonTree = lemonTree
                lemonSize = tree.pick()
                // No inicio nenhum limão foi espremido, então count = 0, depois, count será incrementado em 1
                squeezeCount = 0
            }
            SQUEEZE -> {
                squeezeCount += 1
                lemonSize -= 1

                // Agora o lemonadeState ficará no estado SQUEEZE até completar o limão (lemonSize = 0)
                // Se o lemonSize é 0, então o estado será DRINK até voltar para o estado SQUEEZE
                lemonadeState = if (lemonSize == 0) {
                    DRINK
                } else SQUEEZE
            }
            // Se o estado é DRINK então deve-se beber a limonada e o estado voltara para RESTART
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
            }
            // RESTART significa que teremos que selecionar um limão na árvore
            RESTART -> lemonadeState = SELECT
        }
        // As per result of the above we will call the setViewElements() function and set the view accordingly
        //De acordo com o resultado acima, chamaremos a função setViewElements() e definiremos a visualização de acordo
        setViewElements()

        //lemonImage?.setImageResource(drawableResource)

       //lemonImage?.contentDescription = lemonadeState

        // TODO: Quando a imagem é clicada no estado SQUEEZE, o squeezeCount precisa ser
        //  AUMENTADO em 1 e lemonSize precisa ser DIMINUÍDO em 1.
        //  - Se o lemonSize atingiu 0, ele foi espremido e o estado deve se tornar DRINK
        //  - Além disso, lemonSize não é mais relevante e deve ser definido como -1

        // TODO: Quando a imagem é clicada no estado DRINK o estado deve se tornar RESTART

        // TODO: Quando a imagem é clicada no estado RESTART o estado deve se tornar SELECT

        // TODO: por último, antes que a função termine, precisamos definir os elementos da view para que o
        //  UI pode refletir o estado correto
    }

    /**
     * Set up the view elements according to the state / Configure os elementos de exibição de acordo com o estado.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        val lemonImage: ImageView = findViewById(R.id.image_lemon_state)
        // Configura uma condicional que rastreia o lemonadeState
        when (lemonadeState) {
            SELECT -> {
                // Para cada estado, o textAction TextView deve ser definido para a string correspondente de
                //  o arquivo de recursos de string. As strings são nomeadas para corresponder ao estado
                textAction.text = "Click to select a lemon!"
                // Além disso, para cada estado, o lemonImage deve ser definido para o valor correspondente
                //  desenhável dos recursos desenháveis. Os drawables têm os mesmos nomes que as strings
                //  mas lembre-se que eles são drawables, não strings.
                lemonImage.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.text = "Click to juice the lemon!"
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.text = "Click to drink your lemonade!"
                lemonImage.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                textAction.text = "Click to start again!"
                lemonImage.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD / NÃO ALTERE ESTE MÉTODO ===
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     *
     * Clicar longamente na imagem do limão mostrará quantas vezes o limão foi espremido.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 * * Uma classe de árvore de limão com um método para "escolher" um limão. O "tamanho" do limão é aleatório
 * e determina quantas vezes um limão precisa ser espremido antes de obter a limonada.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
