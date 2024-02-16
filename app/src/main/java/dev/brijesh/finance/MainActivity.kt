@file:OptIn(ExperimentalFoundationApi::class)

package dev.brijesh.finance

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import dev.brijesh.finance.ui.theme.FinanceTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceTheme(dynamicColor = false) {
                Dashboard()
            }
        }
    }
}

@Composable
fun Dashboard() {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(
                painter = painterResource(id = R.drawable.home), contentDescription = "Home",
                modifier = Modifier.size(56.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.menu), contentDescription = "Menu",
                modifier = Modifier
                    .size(56.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(16.dp)
            )
        }

        Text(
            text = "Financial Dashboard",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayLarge,
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    "£10.7k",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Total Balance",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            ConstraintLayout {
                val (link, analytics) = createRefs()

                Icon(
                    painter = painterResource(id = R.drawable.link),
                    contentDescription = "Link",
                    modifier = Modifier
                        .size(100.dp)
                        .background(color = Color.White, shape = CircleShape)
                        .padding(30.dp)
                        .constrainAs(link) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )

                Icon(
                    painterResource(id = R.drawable.analytics),
                    contentDescription = "Analytics",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .background(color = Color.Black, shape = CircleShape)
                        .padding(30.dp)
                        .constrainAs(analytics) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(link.start, margin = 80.dp)
                        }
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1F)
                    .background(Color.White, shape = RoundedCornerShape(percent = 20)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.arrow_right_up),
                    contentDescription = "Withdraw",
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Withdraw",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1F)
                    .background(Color.White, shape = RoundedCornerShape(percent = 20)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.arrow_right_up),
                    contentDescription = "Deposit",
                    modifier = Modifier
                        .size(42.dp)
                        .rotate(180F)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Deposit",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        MonthSelectionSection()

    }
}

data class MonthData(val name: String)

val months = listOf(
    MonthData("January"),
    MonthData("February"),
    MonthData("March"),
    MonthData("April"),
    MonthData("May"),
    MonthData("June"),
    MonthData("July"),
    MonthData("August"),
    MonthData("September"),
    MonthData("October"),
    MonthData("November"),
    MonthData("December"),
)

@Composable
fun MonthSelectionSection() {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { months.size })
    val coroutineScope = rememberCoroutineScope()

    val onTabSelected: (index: Int) -> Unit = { index ->
        coroutineScope.launch {
            pagerState.animateScrollToPage(index)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(percent = 20))
    ) {
        val screenWidth = this.maxWidth
        val tabWidth = (screenWidth.value / 2.5).dp

        Column(Modifier.padding(horizontal = 8.dp)) {

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                edgePadding = tabWidth,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    DotIndicator(tabPositions, pagerState)
                }
            ) {
                months.forEachIndexed { index, monthData ->
                    Tab(
                        text = {
                            Text(
                                text = monthData.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    brush = if (pagerState.currentPage < index) {
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color.Gray,
                                                Color.Transparent
                                            )
                                        )
                                    } else if (pagerState.currentPage > index) {
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Gray
                                            )
                                        )
                                    } else {
                                        null
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        selected = pagerState.currentPage == index,
                        selectedContentColor = Color.Black,
                        onClick = { onTabSelected(index) },
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(20)
                            )
                            .width(tabWidth)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                MonthItem()
            }

        }

    }

}

@Composable
fun DotIndicator(tabPositions: List<TabPosition>, pagerState: PagerState) {
    val transition =
        updateTransition(targetState = pagerState.currentPage, label = "PagerTransition")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            spring(dampingRatio = 1f, stiffness = if (initialState < targetState) 50f else 100f)
        },
        label = ""
    ) {
        tabPositions[it].left
    }
    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            spring(dampingRatio = 1f, stiffness = if (initialState < targetState) 100f else 50f)
        },
        label = ""
    ) {
        tabPositions[it].right
    }

    Box(
        Modifier
            .offset(x = (indicatorEnd + indicatorStart) / 2)
            .wrapContentSize(align = Alignment.BottomStart)
            .size(20.dp)
            .padding(2.dp)
            .border(2.dp, color = Color.Black, CircleShape)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(Color.Black, CircleShape)
        )
    }

}

@Composable
fun MonthItem() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "****1234",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "37",
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "This month",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            ProfileGrid()
        }
    }
}

@Composable
fun ProfileGrid() {
    ConstraintLayout {
        val (first, second, third, forth) = createRefs()

        Box(
            Modifier
                .size(56.dp)
                .background(color = Color.Green, shape = CircleShape)
                .border(2.dp, color = Color.White, shape = CircleShape)
                .constrainAs(forth) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(third.end, margin = 40.dp)
                }
        )
        Box(
            Modifier
                .size(56.dp)
                .background(Color.DarkGray, shape = CircleShape)
                .border(2.dp, color = Color.White, shape = CircleShape)
                .constrainAs(third) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(second.end, margin = 40.dp)
                }
        )
        Box(
            Modifier
                .size(56.dp)
                .background(Color.Blue, shape = CircleShape)
                .border(2.dp, color = Color.White, shape = CircleShape)
                .constrainAs(second) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(first.end, margin = 40.dp)
                }
        )
        Box(
            Modifier
                .size(56.dp)
                .background(Color.Red, shape = CircleShape)
                .border(2.dp, color = Color.White, shape = CircleShape)
                .constrainAs(first) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )

    }
}


@Preview(
    showBackground = true,
    device = "id:pixel_7_pro",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun DashboardPreview() {
    FinanceTheme(dynamicColor = false) {
        Dashboard()
    }
}