# Class Prediction with Linear Discriminant Analysis(LDA)
In this project we tried to **predict** the class of a given point in a plane where we randomly add points in two different colors.
## Video
![](assets/lda.gif)
## How it works?
### Creating visualizing the training set:
* After you provide limit points by given X and Y position of it, app will randomly distribute the points in two different colors on plane. You can use `Limit X` and `Limit Y` inputs.
* You should provide a number of points for the training set for its prediction. App will generate the points as much as this value.
* Blue points represents the class 1. Reds represent the class 2.
* When you hit the `Start` button, you areoing to see the plane with the dots in two different color(blue-red).
### Adding a test point, for prediction the class of it:
* Once you generate the plane you can now add a test point by providing the X and Y position of it.
You can use `Test X` and `Test Y` inputs.
* After that hit `Add` button. Then you should see the green point on the plance.
* Sometimes it is not showing on the plane because of the other points density. You in this case please see the output text window to confirm its add.
* Once you hit add button `Class should be` value should set to what class should be after the prediction. You can use this information as a comparision value with the prediction.
### Starting the analysis:
* Click `Analysis` button for seeing the results of the prediction.
* This willive you two different prediction result. **Discriminant function predicted class** and **Mahalanobis distance predicted class**

#### You can add more test point without reseting the training set and the plane. If you want to start over use `Reset` button.
## Test
| Limit X | Limit Y | Num.Points | Test X | Test Y | Actual Class | Disc. Pred. Class | Mah. Pred. Class |
|---------|---------|------------|--------|--------|--------------|-------------------|------------------|
| 300     | 300     | 1000       | 10     | 10     | 1            | 1(True)           | 1(True)          |
| 300     | 300     | 1000       | 250    | 250    | 1            | 2(False)          | 1(True)          |
| 300     | 300     | 1000       | 280    | 250    | 1            | 2(False)          | 2(False)         |
| 300     | 300     | 1000       | 400    | 250    | 2            | 2(True)           | 2(True)          |