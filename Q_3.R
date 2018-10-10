
check<-train_dataset_without_missing_target[which(train_dataset_without_missing_target$n_issues!=train_dataset_without_missing_target$n_accounts),]
clean_data <- select(train_dataset_without_missing_target,-c(ids))
clean_data$default <- factor(clean_data$default)
table(clean_data$default)

train<-sample_frac(clean_data,0.7)
rowid<-as.numeric(rownames(train)) # because rownames() returns character
test<-clean_data[-rowid,]

# logistic regression
check_logit <- glm(default ~ ., data = train, family = "binomial")
summary(check_logit)

test$predicted_default <- predict(check_logit, newdata = select(test,-c(default)), type = "response")
test[test$predicted_default<=0.5,'predicted_default']<-0
test[test$predicted_default>0.5,'predicted_default']<-1
check_results<- data.frame(cbind(as.numeric(as.character(test$default)),test$predicted_default))
colnames(check_results) <- c('Actual','Predicted')

library(Metrics)

measure_model_metrics <- function(Actual,Predicted){
  accurate <- accuracy(Actual,Predicted)
  precision <- sum(Predicted & Actual) / sum(Predicted)
  recall <- sum(Predicted & Actual) / sum(Actual)
  fmeasure <- 2 * precision * recall / (precision + recall)
  
  cat('accuracy:   ')
  cat(accurate * 100)
  cat('%')
  cat('\n')
  
  cat('precision:  ')
  cat(precision * 100)
  cat('%')
  cat('\n')
  
  cat('recall:     ')
  cat(recall * 100)
  cat('%')
  cat('\n')
  
  cat('f-measure:  ')
  cat(fmeasure * 100)
  cat('%')
  cat('\n')
  
}


measure_model_metrics(check_results$Actual,check_results$Predicted)
table(check_results$Actual,check_results$Predicted)
library(ROSE)
data_balanced_over <- ovun.sample(default ~ ., data = clean_data, method = "over",N = 69118)$data
table(data_balanced_over$default)

data_balanced_over$default <- factor(data_balanced_over$default)
table(data_balanced_over$default)
library(dplyr)
train<-sample_frac(data_balanced_over,0.7)
rowid<-as.numeric(rownames(train)) # because rownames() returns character
test<-data_balanced_over[-rowid,]

check_logit <- glm(default ~ ., data = train, family = "binomial")
summary(check_logit)
test$predicted_default <- predict(check_logit, newdata = select(test,-c(default)), type = "response")
test[test$predicted_default<=0.5,'predicted_default']<-0
test[test$predicted_default>0.5,'predicted_default']<-1
check_results<- data.frame(cbind(as.numeric(as.character(test$default)),test$predicted_default))
colnames(check_results) <- c('Actual','Predicted')
measure_model_metrics(check_results$Actual,check_results$Predicted)
table(check_results$Actual,check_results$Predicted)

library(car)
vif_df<- data.frame(vif(check_logit))
save(check_logit,file = '~/Downloads/Puzzle/logistic_regression.rda')

#SVM

library(e1071)
model_svm <- svm(default ~ . , train)
pred <- predict(model_svm, train[,-1])
#For svm, we have to manually calculate the difference between actual values (train$y) with our predictions (pred)

error <-as.numeric(as.character(train$default)) - as.numeric(as.character(pred))
svm_error <- sqrt(mean(error^2))
test$predicted_default<-NULL
pred <- predict(model_svm, test[,-1])
measure_model_metrics(as.numeric(as.character(test$default)),as.numeric(as.character(pred)))
save(check_logit,file = '~/Downloads/Puzzle/svm.rda')

#Random forest
library(randomForest)
# Fitting model
fit <- randomForest(default ~ ., train,ntree=50)
summary(fit)
#Predict Output 
predicted<- predict(fit,test[,-1])
measure_model_metrics(as.numeric(as.character(test$default)),as.numeric(as.character(predicted)))
save(check_logit,file = '~/Downloads/Puzzle/random_forest.rda')
