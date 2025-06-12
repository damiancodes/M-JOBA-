# MJOBA App Documentation

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge&logo=firebase&logoColor=white" alt="MVVM" />
  <img src="https://img.shields.io/badge/Payment-M--Pesa-4CD964?style=for-the-badge&logo=mpesa&logoColor=white" alt="M-Pesa" />
  
  <p>Connecting urban Kenyans with verified local service providers</p>
  
  <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/home1.jpeg?raw=true" width="300" alt="MJOBA App Screenshot"/>
</div>

##  Overview

MJOBA is a marketplace app that bridges the gap between urban Kenyan customers and local service providers. Operating in busy urban centers like Gikomba, Toi Market, Nairobi CBD, and Lavington, the app provides instant access to verified service professionals including:

- Beauticians
- Plumbers
- Electricians  
- Cobblers
- And more local services

## 🛠️ Technical Architecture

<div align="center">
  <table>
    <tr>
      <th>Component</th>
      <th>Technologies</th>
    </tr>
    <tr>
      <td>Frontend</td>
      <td>
        <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" />
        <img src="https://img.shields.io/badge/Material_Design-757575?style=flat-square&logo=materialdesign&logoColor=white" />
      </td>
    </tr>
    <tr>
      <td>Backend</td>
      <td>
        <img src="https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black" />
        <img src="https://img.shields.io/badge/Cloud_Functions-4285F4?style=flat-square&logo=googlecloud&logoColor=white" />
      </td>
    </tr>
    <tr>
      <td>Database</td>
      <td>
        <img src="https://img.shields.io/badge/Firestore-FFCA28?style=flat-square&logo=firebase&logoColor=black" />
      </td>
    </tr>
    <tr>
      <td>Authentication</td>
      <td>
        <img src="https://img.shields.io/badge/Firebase_Auth-FFCA28?style=flat-square&logo=firebase&logoColor=black" />
        <img src="https://img.shields.io/badge/SMS_Verification-FF6F00?style=flat-square&logo=africas-talking&logoColor=white" />
      </td>
    </tr>
    <tr>
      <td>Maps & Location</td>
      <td>
        <img src="https://img.shields.io/badge/Google_Maps_API-4285F4?style=flat-square&logo=googlemaps&logoColor=white" />
      </td>
    </tr>
    <tr>
      <td>Payments</td>
      <td>
        <img src="https://img.shields.io/badge/M--Pesa_Daraja_API-4CD964?style=flat-square&logo=mpesa&logoColor=white" />
      </td>
    </tr>
    <tr>
      <td>Notifications</td>
      <td>
        <img src="https://img.shields.io/badge/Firebase_Cloud_Messaging-FFCA28?style=flat-square&logo=firebase&logoColor=black" />
      </td>
    </tr>
  </table>
</div>

##  Key Features

<div align="center">
  <table>
    <tr>
      <td align="center" width="20%">
        <img src="https://img.shields.io/badge/-Instant_Booking-007BFF?style=flat-square&logoColor=white" />
        <p>Book immediately or schedule services for later</p>
      </td>
      <td align="center" width="20%">
        <img src="https://img.shields.io/badge/-Escrow_Payments-28A745?style=flat-square&logoColor=white" />
        <p>Secure M-Pesa transactions with funds held until job completion</p>
      </td>
      <td align="center" width="20%">
        <img src="https://img.shields.io/badge/-Provider_Verification-FFC107?style=flat-square&logoColor=white" />
        <p>All service providers are vetted and verified</p>
      </td>
      <td align="center" width="20%">
        <img src="https://img.shields.io/badge/-Ratings_&_Reviews-DC3545?style=flat-square&logoColor=white" />
        <p>Quality assurance through customer feedback</p>
      </td>
      <td align="center" width="20%">
        <img src="https://img.shields.io/badge/-Provider_Dashboard-6610F2?style=flat-square&logoColor=white" />
        <p>Earnings tracking and job management</p>
      </td>
    </tr>
  </table>
</div>

## 🔄 App Workflow

### Customer Journey

```mermaid
graph TD
    A[Download App] --> B[Create Account]
    B --> C[Browse Service Providers]
    C --> D[Select Provider]
    D --> E[Book Service]
    E --> F[Pay via M-Pesa]
    F --> G[Service Fulfilled]
    G --> H[Confirm Completion]
    H --> I[Rate & Review]
```

### Provider Journey

```mermaid
graph TD
    A[Register as Provider] --> B[Complete Verification]
    B --> C[Set Services & Pricing]
    C --> D[Receive Booking Requests]
    D --> E[Accept/Decline Jobs]
    E --> F[Complete Service]
    F --> G[Customer Confirms]
    G --> H[Receive Payment]
    H --> I[View Dashboard Analytics]
```

##  Business Model

<div align="center">
  <table>
    <tr>
      <th>Revenue Stream</th>
      <th>Description</th>
    </tr>
    <tr>
      <td>Commission</td>
      <td>10% fee on each successful service transaction</td>
    </tr>
    <tr>
      <td>Provider Subscriptions</td>
      <td>Optional premium tiers for increased visibility and features</td>
    </tr>
    <tr>
      <td>Boosted Listings</td>
      <td>Paid promotion to appear at top of search results</td>
    </tr>
  </table>
</div>

##  Security Features

- **Secure Payments**: M-Pesa integration with escrow functionality
- **Identity Verification**: Multi-step provider verification process
- **User Authentication**: Firebase Authentication with phone verification
- **Data Protection**: Secure credential storage and encryption
- **Transaction Monitoring**: Automated flagging of suspicious activities

## 📊 Market Impact

MJOBA aims to digitize Kenya's informal economy by:

- Providing local service providers access to wider urban markets
- Building trust through verified profiles and customer reviews
- Creating consistent income opportunities for skilled workers
- Offering convenient, secure service bookings for urban residents

## 🚀 Roadmap

### MVP (Current Focus)
- Core booking functionality
- M-Pesa payment integration
- Provider verification system
- Rating and review system
- Basic provider dashboards

### Future Expansions
- Service categories expansion
- In-app messaging
- Loyalty rewards program
- Advanced analytics for providers
- Subscription tiers and premium features

##  Implementation Details

### Architecture
MJOBA follows the MVVM (Model-View-ViewModel) architecture pattern:

```
app/
├── data/
│   ├── repository/    # Data repositories
│   ├── model/         # Data models
│   └── remote/        # API services (Firebase, M-Pesa)
├── domain/
│   ├── usecase/       # Business logic
│   └── model/         # Domain models
├── presentation/
│   ├── ui/            # Screens and components
│   ├── viewmodel/     # ViewModels
│   └── navigation/    # Navigation components
└── util/              # Utility classes
```

### Key Dependencies

- **UI**: Jetpack Compose, Material Design Components
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Retrofit, OkHttp
- **Async**: Kotlin Coroutines, Flow
- **DI**: Hilt
- **Database**: Firebase Firestore
- **Maps**: Google Maps Compose
- **Payments**: M-Pesa Daraja SDK
- **Analytics**: Firebase Analytics
- **Testing**: JUnit, Espresso, Mockito

##  Screen Showcase

<div align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <strong>Registration</strong><br>
        <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/register.jpeg?raw=true" width="250" alt="Registration Screen"/>
      </td>
      <td align="center" width="33%">
        <strong>Home Screen</strong><br>
        <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/home1.jpeg?raw=true" width="250" alt="Home Screen"/>
      </td>
      <td align="center" width="33%">
        <strong>Location Search</strong><br>
        <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/searchlocation.jpeg?raw=true" width="250" alt="Location Search"/>
      </td>
    </tr>
    <tr>
      <td align="center" width="33%">
        <strong>Booking Summary</strong><br>
        <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/bookingsummary.jpeg?raw=true" width="250" alt="Booking Summary"/>
      </td>
      <td align="center" width="33%">
        <strong>Provider Services</strong><br>
        <img src="https://github.com/damiancodes/M-JOBA-/blob/master/app/src/main/res/drawable/providerservives.jpeg?raw=true" width="250" alt="Provider Services"/>
      </td>
      <td align="center" width="33%">
        <strong>Provider Dashboard</strong><br>
        <img src="https://firebasestorage.googleapis.com/v0/b/mjoba-3daad.firebasestorage.app/o/provider2.jpeg?alt=media&token=32bbcf7a-f1e4-4ead-86bd-6428ba92b4ea" width="250" alt="Provider Dashboard"/>
      </td>
    </tr>
  </table>
</div>

##  Target Audience

### Customers
- Urban residents in Nairobi
- Time-conscious professionals
- People seeking verified service providers

### Service Providers
- Skilled workers in various trades
- Small business owners
- Individual service professionals

##  Contact & Support

For technical support or feature requests:
- Email: support@mjoba.com
- In-app feedback form
- GitHub issues (for developers)

---

<div align="center">
  <p>MJOBA: Connecting Service Needs with Verified Providers</p>
  <p><small>© 2025 MJOBA. All rights reserved.</small></p>
</div>
