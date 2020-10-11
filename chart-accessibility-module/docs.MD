
# Purpose
CAM module is an independent module that can be plugged into existing data sources / charting libraries to generate descriptions for common types of data. 

## Structure
The `IDescriptor` interface is the common binding which has one `describe(): String` method that must be implemented. 

## Usage
```kotlin
val barChartDescriptorDescription = BarChartDescriptor(
            arrayOf("Jazz", "City", "Accord", "HRV"),
            arrayOf(333f, 3223f, 234f, 342f),
            "Car model",
            "sale count",
            "Honda Car model sales count for 2020"
    ).describe()

    println(barChartDescriptorDescription)
```

## Implementing a custom descriptor
```kotlin
class ToImplementDescriptor : IDescriptor {
    
    // Do anything here... 
    
    override fun describe(): String {
        return "please implement accessibility"
    }

}
```


## Usage with MPAndroidChart Modification

`getAccessibilityDescription()` is automatically called by the base `Chart.java` class when the screen-reader requests to populate an accessibility event when focused.

* Override the `getAccessibilityDescription()`
* Use a relevant descriptor or make your own 
* Return the output of `describe()`

```java
class CustomPieChart extends PieChart {
/// other code goes here
    @Override
    public String getAccessibilityDescription() {

        // Gather all relevant data from chart for the descriptor
        PieData pieData = getData();
        IPieDataSet dataSet = pieData.getDataSetByIndex(0);
        String categoryTitleDataset = dataSet.getLabel();

        int entryCount = pieData.getEntryCount();

        Object[] labels = new Object[entryCount];
        Float[] proportions = new Float[entryCount];

        for (int i = 0; i < entryCount; i++) {
            PieEntry entry =dataSet.getEntryForIndex(i);
            float percentage = (entry.getValue() / pieData.getYValueSum());
            labels[i] = entry.getLabel();
            proportions[i] = percentage;
        }
        
        String categoryTitleMerged = categoryTitle.isEmpty() ? categoryTitleDataset : categoryTitle;
        
        
        // Using chart state, generate the descriptor and describe it. 
        IDescriptor pieChartDescriptor = new PieChartDescriptor(
                labels, proportions, categoryTitleMerged
        );


        return pieChartDescriptor.describe();
    }
}

```

```kotlin

private class CustomPieChart(context: Context) : PieChart(context) {

    override fun getAccessibilityDescription(): String {

        val pieData = data
        val dataSet = pieData.getDataSetByIndex(0)
        val categoryTitleDataset = dataSet.label

        val entryCount = pieData.entryCount

        val labels = arrayOfNulls<Any>(entryCount)
        val proportions = arrayOfNulls<Float>(entryCount)

        for (i in 0 until entryCount) {
            val entry = dataSet.getEntryForIndex(i)
            val percentage = entry.value / pieData.yValueSum
            labels[i] = entry.label
            proportions[i] = percentage
        }
        
        val pieDescriptor = PieDescriptor()


        return pieDescriptor.describe(labels, proportions, "category_title")
    }
}

```




