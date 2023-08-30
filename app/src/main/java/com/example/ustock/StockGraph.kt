package com.example.ustock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data_structures.wallet_model.Stock
import data_structures.wallet_model.StockDataPoint
import okhttp3.Protocol.Companion.get
import java.util.Calendar
import java.util.Date

val stock: Stock
    get() {
        TODO()
        //GET API call to call for the stocks
        //Print the values retrieved from the API so I know how to parse it



    }
var isCurrentUser: Boolean = false
var timeRange: TimeRange = TimeRange.ONE_MONTH

@Composable
fun LineGraph(data: List<Float>, modifier: Modifier = Modifier) {

    Canvas(modifier = modifier.padding(16.dp)) {
        val path = Path()
        val strokeWidth = Stroke(width = 2f)
        val lineColor = Color.Blue

        if (data.isNotEmpty()) {
            val stepX = size.width / (data.size - 1)
            val maxY:Float = data.maxOrNull() ?: 1f
            val minY:Float = data.minOrNull() ?: 0f
            val valueRange = maxY - minY

            data.forEachIndexed { index, value ->
                val x = index * stepX
                val y = size.height - ((value - minY) / valueRange) * size.height
                val point = Offset(x, y)

                if (index == 0) {
                    path.moveTo(point.x, point.y)
                } else {
                    path.lineTo(point.x, point.y)
                }
            }
        }

        drawPath(path = path, color = lineColor, style = strokeWidth)
    }
}



enum class TimeRange(val value: String) {
    FIVE_DAYS("5D"),
    ONE_WEEK("1W"),
    ONE_MONTH("1M"),
    ONE_YEAR("1Y"),
    ALL("ALL")
}



val paddingX: Float = 30f
val paddingY: Float = 20f

var isBouncing = false // animation
var stockIsLoading = false
var isCheckMark = false // Add this to track checkmark animation

var isShowingTradeStockView = false

fun getFilteredHistory(stock: Stock, timeRange: TimeRange): List<StockDataPoint> {
    val now = Date()
    val calendar = Calendar.getInstance()
    val endDate: Date = when (timeRange) {
        TimeRange.FIVE_DAYS -> calendar.apply { add(Calendar.DAY_OF_YEAR, -5) }.time
        TimeRange.ONE_WEEK -> calendar.apply { add(Calendar.WEEK_OF_YEAR, -1) }.time
        TimeRange.ONE_MONTH -> calendar.apply { add(Calendar.MONTH, -1) }.time
        TimeRange.ONE_YEAR -> calendar.apply { add(Calendar.YEAR, -1) }.time
        TimeRange.ALL -> calendar.apply { add(Calendar.YEAR, -100) }.time
    }

    val filtered = stock.history.filter { it.date >= endDate }

    return if (filtered.isEmpty()) stock.history else filtered
}


//Create line graph with varying time lengths
@Composable
fun createLineGraph(timeRange: TimeRange){
    var filteredHistory = getFilteredHistory(stock, timeRange)
    val data = mutableListOf<Float>()
    for (i in filteredHistory){
        data.add(i.value.toFloat())
    }
    //Line graph
    LineGraph(data = data, modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .offset(x = -30.dp, y = 0.dp))
}


@Preview(showBackground = true)
@Composable
fun StockGraphWithControls() {
    //var data by remember { mutableStateOf(listOf(100, 75, 20, 50, 70, 60, 80)) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("STOCK", fontSize = 18.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Y-axis label
            // Y-axis label
            Text("Value",fontSize = 14.sp,textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .rotate(270f) // Rotate the text by 90 degrees
                    .padding(2.dp)
                    .offset(x = 10.dp, y = 0.dp)
            )

            createLineGraph(timeRange)
        }

        // Buttons to manipulate the x-axis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { timeRange = TimeRange.FIVE_DAYS }) { // Increase all values by 10
                Text("5D")
            }
            Button(onClick = { timeRange = TimeRange.ONE_WEEK }) { // Increase all values by 10
                Text("1W")
            }
            Button(onClick = { timeRange = TimeRange.ONE_MONTH }) { // Increase all values by 10
                Text("1M")
            }
            Button(onClick = { timeRange = TimeRange.ONE_YEAR }) { // Decrease all values by 10
                Text("1Y")
            }
            Button(onClick = {timeRange = TimeRange.ALL}) { // Decrease all values by 10
                Text("ALL")
            }
            createLineGraph(timeRange)
        }
    }
}