var chart;
var chartData = [
    {
        "year": 2009,
        "income": 23.5,
        "expenses": 18.1
    },
    {
        "year": 2010,
        "income": 26.2,
        "expenses": 22.8
    },
    {
        "year": 2011,
        "income": 30.1,
        "expenses": 23.9
    },
    {
        "year": 2012,
        "income": 29.5,
        "expenses": 25.1
    },
    {
        "year": 2013,
        "income": 30.6,
        "expenses": 27.2,
        "dashLengthLine": 5
    },
    {
        "year": 2014,
        "income": 34.1,
        "expenses": 29.9,
        "dashLengthColumn": 5,
        "alpha": 0.2,
        "additional": "(projection)"
    }

];


AmCharts.ready(function () {
    // SERIAL CHART
    chart = new AmCharts.AmSerialChart();

    chart.dataProvider = chartData;
    chart.categoryField = "year";
    chart.startDuration = 1;

    chart.handDrawn = true;
    chart.handDrawnScatter = 3;

    // AXES
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.gridPosition = "start";

    // value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.axisAlpha = 0;
    chart.addValueAxis(valueAxis);

    // GRAPHS
    // column graph
    var graph1 = new AmCharts.AmGraph();
    graph1.type = "column";
    graph1.title = "Ganancia";
    graph1.lineColor = "#e35b5a";
    graph1.valueField = "income";
    graph1.lineAlpha = 1;
    graph1.fillAlphas = 1;
    graph1.dashLengthField = "dashLengthColumn";
    graph1.alphaField = "alpha";
    graph1.balloonText = "<span style='font-size:13px;'>[[title]] in [[category]]:<b>[[value]]</b> [[additional]]</span>";
    chart.addGraph(graph1);

    // line
    var graph2 = new AmCharts.AmGraph();
    graph2.type = "line";
    graph2.title = "Radiografías informadas";
    graph2.lineColor = "#fcd202";
    graph2.valueField = "expenses";
    graph2.lineThickness = 3;
    graph2.bullet = "round";
    graph2.bulletBorderThickness = 3;
    graph2.bulletBorderColor = "#fcd202";
    graph2.bulletBorderAlpha = 1;
    graph2.bulletColor = "#ffffff";
    graph2.dashLengthField = "dashLengthLine";
    graph2.balloonText = "<span style='font-size:13px;'>[[title]] in [[category]]:<b>[[value]]</b> [[additional]]</span>";
    chart.addGraph(graph2);

    // LEGEND
    var legend = new AmCharts.AmLegend();
    legend.useGraphSettings = true;
    chart.addLegend(legend);

    // WRITE
    chart.write("chartdiv");
});