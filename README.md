# My Perishable Planner

Design Document

Archana Thapa, Jacob Farrish, Austin Kirk, Gabriel Phan, Matthew Abbott

## Introduction

Do you often forget about products in your home/office that expire or reach their sell by date? Do you look for a solution to keeping track of all the expiration dates? My Perishable Planner can assist you with:

- Writing down expiration/sell by dates.
- Tracking when products expire.
- Notifying you when a product expires and needs replacing.

Use your Android Device to log an item via picture,barcode, or name. Enter an expiration date. Track all items and see how much time they have till expired. Get notified when items begin to expire or need replaced.

## Storyboard
[MyPerishablePlanner Storyboard](https://projects.invisionapp.com/prototype/ExpirationApp-ckz1rt95r000z1k01yx12rfn7/play/b63ecb76)  

![storyboard_Expiration jpg](https://user-images.githubusercontent.com/83916583/151725052-1169ad71-f04e-4be2-bd54-57cfdd457223.png)
## Functional Requirements

### Requirement #1

#### Scenario

As a user, I want to receive a text message when something goes bad, so that I can replace it at the store.

#### Dependencies

Notifications are enabled.

#### Assumptions

The notifications are sent via text.

Milk will go bad, text will be sent.

#### Examples

1.1

**Given**  that my jelly expires on 2/1/2022

**When**   it becomes 2/2/2022

**Then**  I will receive a text message alerting me to get more jelly

1.2

**Given**  that my lunchmeat expires a week from today

**When**   eight days pass

**Then**   I will receive a text message alerting me to throw out the lunchmeat

### Requirement #2

#### Scenario

As a user, I want to scan a product at the store, so that I can see if my item at home will expire soon.

#### Dependencies

The device has a QR/Barcode reader, user has given access to the camera.

#### Assumptions

Product is entered in the app.

#### Examples

2.1

**Given**  that I grab bread in the store

**When**   I scan the barcode with my phone

**Then**   then I can see the date at which the bread I have at home expires

2.2

**Given**  that I grab eggs in the store

**When**   when I scan the barcode with my phone

**Then**   I can see the date at which the eggs I have at home go bad

## Class Diagram

![diagram1](https://user-images.githubusercontent.com/97859319/151725647-71096518-1fd8-420e-a3b8-6d3e14dec8b7.png)

### Class Diagram Description

- MainActivity: This is the main screen that the user views when starting the application. It consists of a list of items they have entered arranged by the date in which they go bad.
- RetroFitClientInstance: This is just a bootstrap instance that is required to make retrofit work properly.
- ExpirationDetailActivity: A screen that opens when a specific perishable is selected. It shows a more detailed insight of the item, mainly its expiration date.
- IPerishableDAO: Interface that allows RetroFit to find and parse Perishable JSON
- IExpirationDAO: Interface for Room to connect Expiration data
- Perishable: Noun class representing the perishable item
- Expiration: Noun class representing the items expiration details

## Scrum Roles

- DevOps/Product Owner/Scrum Master: Gabriel Phan
- UI Specialist: Archana Thapa & Jacob Farrish
- Integration Specialist: Matthew Abbott & Austin Kirk

## Weekly Meeting

Tuesday and Thursday @ 7:30 pm

