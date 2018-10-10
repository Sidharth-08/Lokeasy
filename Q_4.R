library(dplyr)
# default
# false=0
# true=1

puzzle_test_dataset[puzzle_test_dataset$default==FALSE,'default']<-0
puzzle_test_dataset[puzzle_test_dataset$default==TRUE,'default']<-1

levels(puzzle_test_dataset$score_1)
#[1] ""                         "1Rk8w4Ucd5yR3KcqZzLdow==" "4DLlLW62jReXaqbPaHp1vQ=="
#[4] "8k8UDR4Yx0qasAjkGrUZLw==" "DGCQep2AE5QRkNCshIAlFQ==" "e4NYDor1NOw6XKGE60AWFw=="
#[7] "fyrlulOiZ+5hoFqLa6UbDQ==" "smzX0nxh5QlePvtVf6EAeg=="
puzzle_test_dataset$score_1<-as.character(puzzle_test_dataset$score_1)
puzzle_test_dataset[puzzle_test_dataset$score_1=='','score_1']<-1
puzzle_test_dataset[puzzle_test_dataset$score_1=='1Rk8w4Ucd5yR3KcqZzLdow==','score_1']<-2
puzzle_test_dataset[puzzle_test_dataset$score_1=='4DLlLW62jReXaqbPaHp1vQ==','score_1']<-3
puzzle_test_dataset[puzzle_test_dataset$score_1=='8k8UDR4Yx0qasAjkGrUZLw==','score_1']<-4
puzzle_test_dataset[puzzle_test_dataset$score_1=='DGCQep2AE5QRkNCshIAlFQ==','score_1']<-5
puzzle_test_dataset[puzzle_test_dataset$score_1=='e4NYDor1NOw6XKGE60AWFw==','score_1']<-6
puzzle_test_dataset[puzzle_test_dataset$score_1=='fyrlulOiZ+5hoFqLa6UbDQ==','score_1']<-7
puzzle_test_dataset[puzzle_test_dataset$score_1=='smzX0nxh5QlePvtVf6EAeg==','score_1']<-8
puzzle_test_dataset$score_1 <- as.numeric(as.character(puzzle_test_dataset$score_1))

onehotencoder<- function(df,col){
  count_unique<- length(levels(df[,col])) 
  levels_list<-list(levels(df[,col]))
  print(levels(df[,col]))
  df[,col]<-as.character(df[,col])
  for(i in 1:count_unique){
    df[df[,col]==levels_list[[1]][i],col]<-i
  }
  df[,col]<-as.numeric(df[,col])
  return(df)
  
}

puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'score_2')
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'reason')
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'gender')

table(puzzle_test_dataset$sign)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'sign')

table(puzzle_test_dataset$facebook_profile)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'facebook_profile')

table(puzzle_test_dataset$state)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'state')

table(puzzle_test_dataset$zip)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'zip')

table(puzzle_test_dataset$job_name)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'job_name')

table(puzzle_test_dataset$real_state)
puzzle_test_dataset<-onehotencoder(puzzle_test_dataset,'real_state')

puzzle_test_dataset$end_last_loan <- as.Date(as.character(puzzle_test_dataset$end_last_loan))
puzzle_test_dataset$last_payment <- as.Date(as.character(puzzle_test_dataset$last_payment))
library(lubridate)
puzzle_test_dataset$last_payment_year <- year(puzzle_test_dataset$last_payment)
puzzle_test_dataset$last_payment_month <- month(puzzle_test_dataset$last_payment)
puzzle_test_dataset$end_last_loan_year <- year(puzzle_test_dataset$end_last_loan)
puzzle_test_dataset$end_last_loan_month <- month(puzzle_test_dataset$end_last_loan)

puzzle_test_dataset <- select(puzzle_test_dataset,-c(last_payment,end_last_loan,channel))
summary(puzzle_test_dataset)

for(i in 2:ncol(puzzle_test_dataset)){
  puzzle_test_dataset[is.na(puzzle_test_dataset[,i]), i] <- median(puzzle_test_dataset[,i], na.rm = TRUE)
}

puzzle_test_dataset$predicted_default<- predict(fit,puzzle_test_dataset[,-1])
test_output<- data.frame(cbind(as.character(puzzle_test_dataset$ids),as.character(puzzle_test_dataset$predicted_default)))
colnames(test_output) <- c('ids','Predicted_default')
write.csv(test_output,'~/Downloads/Puzzle/test_output.csv',row.names = FALSE)

length(which(test_output$ids %in% as.character(big_case_train$ids)))
