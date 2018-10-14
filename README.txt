Hello! Welcome to Emo. Our application is currently facing difficulties running with Samsung phones, but works great with Google phones!

## Inspiration
Social media and technology affect different people in different ways. For those affected negatively by social media in some form, cutting out technology as a whole or reducing all technology usage is a difficult and unnecessary and often prevents people from taking steps to protect their mental health. By allowing user's to see how different applications affect them in different ways, users will be able to personalize their online experience to maximize their own well being. 

## What it does
We take a biometric approach to the idea of understanding a person's emotional state. By analyzing facial expressions, we can understand people's underlying emotions. We track these emotions frequently while users use their phone and then we display their data by emotion and allow them to view their emotions over time as they use a particular application. 

We are helping people who are negatively impacted by certain forms of social media or technology in an avoidable way. Emo provides the information that allows people to shape their life.


## How we built it
We built an Android application that used various Android tools to take photos as part of a background process while identifying which application was being used at the time. From there, we utilized Google Cloud Vision to annotate our images and allow us to retrieve various sentiments. We processed these sentiments and graphed them in multiple ways for our users, including by application and by time. 

## Challenges we ran into
We ran into challenges with our phones that prevented us from using our own phones to test our application. The issues would cause a disconnect between Google Cloud and our application when using Samsung devices (which we all have). Thankfully, these problems did not occur in our similarly situated emulator! 

We also ran into multiple problems relating to our data persistence over time as our application was running in the background. These issues are completely resolved 

## Accomplishments that we're proud of
We were able to create a complex back end for our application that saved users data efficiently and properly over time and a simple but effective front end. 

## What's next for emo
The advantage of facial analysis within emo is that each user will almost always be the only person using their phone, so we have continuity between the faces that we are analyzing. This advantage can be used to create a more personalized facial analysis algorithm for a user as they use emo. 