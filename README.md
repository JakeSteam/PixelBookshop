# Pixel Bookshop

This is an open source fork of my abandoned project Pixel Bookshop, feel free to do whatever you want with it!

## Screenshots
| Main screen | Message log | Stockroom | Video |
| -- | -- | -- | -- | 
| ![image](https://user-images.githubusercontent.com/12380876/157321760-793f4e6c-2e6a-42a1-af43-0c66ed4497c5.png) | ![image](https://user-images.githubusercontent.com/12380876/157321833-0c3b6d1f-40d6-43b9-b266-5e5f795f1c3b.png) | ![image](https://user-images.githubusercontent.com/12380876/157321870-ae36afc3-7bd4-495a-910c-a6bb0546f534.png) | https://user-images.githubusercontent.com/12380876/157321937-97543fa7-094b-4031-b15d-19b4d093d10a.mp4 |

## Codebase notes
* The codebase has an unfinished & poorly implemented MVVM structure.
* Uses Room & enums extensively. 
* `/scripts/BookGenerator.kt` is a script to contact the Goodreads API for an abritrary book, parse the book data, and output valid Kotlin code for use in the `Book` enum!

## Licensing
* Entire repository is under the MIT license, essentially do whatever you want but don't blame me if it breaks!
* All images are modified versions of [Kenney](https://www.kenney.nl/assets/isometric-library-tiles) assets.
