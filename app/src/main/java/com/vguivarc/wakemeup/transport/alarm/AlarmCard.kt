package com.vguivarc.wakemeup.transport.alarm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Alarm

@Preview
@Composable
fun AlarmCardPreview() {
    AlarmCard(
        viewModel = null,
        alarm = Alarm(1),
        editing = true
    )
}

@Composable
fun AlarmCard(viewModel: AlarmListViewModel?, alarm: Alarm, editing: Boolean) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        val (delete,divider, itemAlarmDaysList, editAlarmArrow, itemAlarmTime, itemAlarmDays, itemAlarmSwitch) = createRefs()

        Text(
            text = alarm.getHeureTexte(),
            modifier = Modifier
                .constrainAs(itemAlarmTime) {
                    start.linkTo(parent.start, margin = 16.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                }
                .clickable { viewModel?.actionChangeAlarmTime(alarm) },
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp
        )
        Switch(
            checked = alarm.isActif,
            onCheckedChange = {
                viewModel?.actionSwitchAlarm(alarm) },
            enabled = true,
            modifier = Modifier.constrainAs(itemAlarmSwitch) {
                end.linkTo(parent.end, margin = 16.dp)
                top.linkTo(parent.top, margin = 0.dp)
                bottom.linkTo(itemAlarmTime.bottom, margin = 0.dp)

            }
        )
        if(editing) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(itemAlarmDaysList) {
                            top.linkTo(itemAlarmDays.bottom, margin = 16.dp)
                        }
                ) {
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Monday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Tuesday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Wednesday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Thursday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Friday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Saturday)
                    DayButton(viewModel, alarm, Alarm.DaysWeek.Sunday)
                }

            Row(
                modifier = Modifier
                    .clickable { viewModel?.actionRemoveAlarm(alarm)}
                    .constrainAs(delete) {
                        top.linkTo(editAlarmArrow.top, margin = 0.dp)
                        bottom.linkTo(editAlarmArrow.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete_black_24dp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(text = "Supprimer", modifier = Modifier.padding(16.dp, 0.dp))
            }
        }
        Image(
            colorFilter = ColorFilter.tint(
                MaterialTheme.colors.primary
            ),
            painter = painterResource(R.drawable.ic_baseline_arrow_drop_down_24),
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { viewModel?.actionEditAlarm(alarm) }
                .constrainAs(editAlarmArrow) {
                    start.linkTo(itemAlarmSwitch.start, margin = 0.dp)
                    end.linkTo(itemAlarmSwitch.end, margin = 0.dp)
                    if(editing) {
                            top.linkTo(itemAlarmDaysList.bottom, margin = 16.dp)
                    } else {
                            top.linkTo(itemAlarmTime.bottom, margin = 8.dp)
                    }
                }
                .size(32.dp)
                .rotate(
                    if (editing) {
                        180f
                    } else {
                        0f
                    }
                )
                .border(1.5.dp, MaterialTheme.colors.primary, CircleShape)
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .constrainAs(divider) {
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(editAlarmArrow.bottom, margin = 0.dp)
                }
                .width(1.dp)
        )
        Text(text = alarm.getJoursTexte(),
            maxLines = 1,
            modifier = Modifier
            .constrainAs(itemAlarmDays) {
            top.linkTo(itemAlarmTime.bottom, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(editAlarmArrow.start, margin = 16.dp)
        })

    }
}

@Composable
fun DayButton(viewModel: AlarmListViewModel?, alarm: Alarm, day: Alarm.DaysWeek){
        Button(
            onClick = { viewModel?.actionDaySelected(alarm, day) },
            modifier = Modifier.size(40.dp),  //avoid the oval shape
            shape = CircleShape,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = if(alarm.listActifDays.contains(day)) {
                ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    backgroundColor = MaterialTheme.colors.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.primary,
                    backgroundColor = Color.White
                )
            }
        ) {
            Text(day.name.first().toString())
        }
}
