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

### Requirement

#### Scenario



#### Dependencies



#### Assumptions





#### Examples


## Class Diagram


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

https://discord.gg/s42A6MCC
